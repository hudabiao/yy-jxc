package io.renren.modules.erp.dao;

import io.renren.modules.erp.domain.ErpInStockLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【erp_in_stock_log(入库记录)】的数据库操作Mapper
* @createDate 2023-02-16 19:18:02
* @Entity io.renren.modules.erp.domain.ErpInStockLog
*/
@Mapper
public interface ErpInStockLogMapper extends BaseMapper<ErpInStockLog> {

}




