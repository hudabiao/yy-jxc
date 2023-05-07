package io.renren.modules.erp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsSellDto {
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 售价
     */
    private BigDecimal sellingPrice;
    /**
     * 销售数量
     */
    private Integer sellCount;

    private Integer paymentMethod;
}
