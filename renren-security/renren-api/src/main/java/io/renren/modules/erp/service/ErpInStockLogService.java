package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.domain.ErpInStockLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_in_stock_log(入库记录)】的数据库操作Service
* @createDate 2023-02-16 19:18:02
*/
public interface ErpInStockLogService extends IService<ErpInStockLog> {
    void saveByGoodList(Long goodsId, int count);
}
