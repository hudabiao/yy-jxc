package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础商品
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class BaseGoodsExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "商品名称")
    private String goodsName;
    @Excel(name = "分类id")
    private Integer categoryId;
    @Excel(name = "采购单价")
    private BigDecimal purchasePrice;
    @Excel(name = "销售单价（定价）")
    private BigDecimal sellingPrice;
    @Excel(name = "图片")
    private String picture;
    @Excel(name = "条码")
    private String barCode;

}