package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpSellLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【erp_sell_log(销售记录)】的数据库操作Service
 * @createDate 2023-02-16 19:18:02
 */
public interface ErpSellLogService extends IService<ErpSellLog> {
    List<ErpSellLog> selectSellLogList(Date startTime, Date endTime, Long shopId);
}
