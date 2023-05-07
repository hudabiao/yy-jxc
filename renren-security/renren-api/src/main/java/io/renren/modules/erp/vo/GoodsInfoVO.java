package io.renren.modules.erp.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsInfoVO {
    private Long goodsId;
    private String goodsName;
    private BigDecimal sellingPrice;
    private String picture;
    private Integer count;
}
