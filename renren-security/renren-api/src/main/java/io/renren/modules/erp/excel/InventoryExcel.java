package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 库存
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class InventoryExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "商品id")
    private Long goodsId;
    @Excel(name = "数量")
    private String amount;

}