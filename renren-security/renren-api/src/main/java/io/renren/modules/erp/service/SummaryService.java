package io.renren.modules.erp.service;

import io.renren.enums.PaymentMethodEnum;
import io.renren.modules.erp.bo.SellSummaryBO;
import io.renren.modules.erp.domain.ErpSellLog;
import io.renren.modules.erp.vo.AllSellSummaryVO;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    private ErpSellLogService erpSellLogService;

    private SysRoleUserService sysRoleUserService;

    private SysDeptService sysDeptService;

    public SummaryService(ErpSellLogService erpSellLogService, SysRoleUserService sysRoleUserService,
                          SysDeptService sysDeptService) {
        this.erpSellLogService = erpSellLogService;
        this.sysRoleUserService = sysRoleUserService;
        this.sysDeptService = sysDeptService;
    }

    /**
     * 统计（销售情况）
     *
     * @param queryDate 查询日期
     * @return {@link SellSummaryBO}
     * @throws ParseException 解析异常
     */
    public SellSummaryBO summary(String queryDate) throws ParseException {
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotBlank(queryDate)) {
            startTime = DateUtils.parseDate(queryDate, "yyyy-MM-dd");
        } else {
            startTime = new Date();
            String format = DateFormatUtils.format(startTime, "yyyy-MM-dd");
            startTime = DateUtils.parseDate(format, "yyyy-MM-dd");
        }
        endTime = DateUtils.addDays(startTime, 1);
        final Long userId = SecurityUser.getUserId();
        //用户角色列表
        List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
        Long shopSysRoleId = 1638140574869532674L;
        Long deptId = SecurityUser.getDeptId();
        if(roleIdList.contains(shopSysRoleId)){
            deptId = null;
        }
        return getSellSummaryBO(startTime, endTime, deptId);
    }

    public List<AllSellSummaryVO> allSummary(Date startDate, Date endDate) {
        long pid = 1067246875800000066L;
        List<SysDeptEntity> list = sysDeptService.listByPid(pid);
        final List<AllSellSummaryVO> collect = list.stream().map(dept -> {
            SellSummaryBO sellSummaryBO = getSellSummaryBO(startDate, endDate, dept.getId());
            final AllSellSummaryVO allSellSummaryVO = new AllSellSummaryVO();
            BeanUtils.copyProperties(sellSummaryBO, allSellSummaryVO);
            allSellSummaryVO.setDeptName(dept.getName());
            allSellSummaryVO.setDeptId(dept.getId());
            return allSellSummaryVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @NotNull
    private SellSummaryBO getSellSummaryBO(Date startTime, Date endTime, Long deptId) {
        final List<ErpSellLog> erpSellLogs = erpSellLogService.selectSellLogList(startTime, endTime, deptId);

        int sellAmount = erpSellLogs.stream().mapToInt(ErpSellLog::getSellCount).sum();
        // 总收入
        BigDecimal totalIncome = erpSellLogs.stream().map(computeIncome()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总利润
        BigDecimal totalProfit = erpSellLogs.stream().map(computeProfit()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 微信
        BigDecimal wechatIncome = getIncomeByType(erpSellLogs, PaymentMethodEnum.WECHAT.value());
        // 支付宝
        BigDecimal alipayIncome = getIncomeByType(erpSellLogs, PaymentMethodEnum.ALIPAY.value());
        // 现金
        BigDecimal cashIncome = getIncomeByType(erpSellLogs, PaymentMethodEnum.CASH.value());
        // 其他
        BigDecimal otherIncome = getIncomeByType(erpSellLogs, PaymentMethodEnum.OTHER.value());
        final SellSummaryBO sellSummaryBO = new SellSummaryBO();
        sellSummaryBO.setSellAmount(sellAmount);
        sellSummaryBO.setTotalIncome(totalIncome);
        sellSummaryBO.setTotalProfit(totalProfit);
        sellSummaryBO.setWechatIncome(wechatIncome);
        sellSummaryBO.setAlipayIncome(alipayIncome);
        sellSummaryBO.setCashIncome(cashIncome);
        sellSummaryBO.setOtherIncome(otherIncome);
        return sellSummaryBO;
    }


    private BigDecimal getIncomeByType(List<ErpSellLog> erpSellLogs, int method) {
        return erpSellLogs.stream().filter(log -> method == log.getPaymentMethod())
                .map(computeIncome()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @NotNull
    private Function<ErpSellLog, BigDecimal> computeIncome() {
        return log -> {
            BigDecimal actualPaidPrice = log.getActualPaidPrice();
            Integer sellCount = log.getSellCount();
            BigDecimal count = new BigDecimal(sellCount);
            return actualPaidPrice.multiply(count);
        };
    }

    @NotNull
    private Function<ErpSellLog, BigDecimal> computeProfit() {
        return log -> {
            BigDecimal actualPaidPrice = log.getActualPaidPrice();
            BigDecimal purchasePrice = log.getPurchasePrice();
            BigDecimal divide = actualPaidPrice.subtract(purchasePrice);
            Integer sellCount = log.getSellCount();
            BigDecimal count = new BigDecimal(sellCount);
            return divide.multiply(count);
        };
    }
}
