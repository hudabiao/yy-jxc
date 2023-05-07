package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售记录
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class SellLogExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "商品id")
    private Long goodsId;
    @Excel(name = "销售时间")
    private Date sellTime;
    @Excel(name = "支付方式")
    private Integer paymentMethod;
    @Excel(name = "实际支付单价")
    private BigDecimal actualPaidPrice;
    @Excel(name = "数量")
    private Integer sellCount;

}