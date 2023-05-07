package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 入库记录
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class InStockLogExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "商品id")
    private Long goodsId;
    @Excel(name = "入库时间")
    private Date inStockTime;
    @Excel(name = "入库数量")
    private String inStockCount;

}