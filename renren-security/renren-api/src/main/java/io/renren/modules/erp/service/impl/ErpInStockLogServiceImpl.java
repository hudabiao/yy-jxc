package io.renren.modules.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.erp.domain.ErpInStockLog;
import io.renren.modules.erp.service.ErpInStockLogService;
import io.renren.modules.erp.dao.ErpInStockLogMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Administrator
* @description 针对表【erp_in_stock_log(入库记录)】的数据库操作Service实现
* @createDate 2023-02-16 19:18:02
*/
@Service
public class ErpInStockLogServiceImpl extends ServiceImpl<ErpInStockLogMapper, ErpInStockLog>
    implements ErpInStockLogService{

    @Override
    public void saveByGoodList(Long goodsId, int count) {
        ErpInStockLog inStockLog = new ErpInStockLog();
        inStockLog.setGoodsId(goodsId);
        inStockLog.setInStockCount(count);
        inStockLog.setInStockTime(new Date());
        this.save(inStockLog);
    }
}




