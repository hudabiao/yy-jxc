package io.renren.modules.erp.dao;

import io.renren.modules.erp.bo.SellSummaryBO;
import io.renren.modules.erp.domain.ErpSellLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author Administrator
 * @description 针对表【erp_sell_log(销售记录)】的数据库操作Mapper
 * @createDate 2023-02-16 19:18:02
 * @Entity io.renren.modules.erp.domain.ErpSellLog
 */
@Mapper
public interface ErpSellLogMapper extends BaseMapper<ErpSellLog> {
    SellSummaryBO countSellData(@Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("shopId") Long shopId);
}




