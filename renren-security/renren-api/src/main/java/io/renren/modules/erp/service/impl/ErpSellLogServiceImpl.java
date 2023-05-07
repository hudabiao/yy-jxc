package io.renren.modules.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.erp.domain.ErpSellLog;
import io.renren.modules.erp.service.ErpSellLogService;
import io.renren.modules.erp.dao.ErpSellLogMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_sell_log(销售记录)】的数据库操作Service实现
* @createDate 2023-02-16 19:18:02
*/
@Service
public class ErpSellLogServiceImpl extends ServiceImpl<ErpSellLogMapper, ErpSellLog>
    implements ErpSellLogService {

    @Override
    public List<ErpSellLog> selectSellLogList(Date startTime, Date endTime, Long shopId) {
        return lambdaQuery().eq(shopId != null, ErpSellLog::getShopId, shopId)
                .between(ErpSellLog::getSellTime, startTime, endTime).list();
    }
}




