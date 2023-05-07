package io.renren.modules.erp.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryShopVO {
    /**
     * 商品id
     */
    private Long goodsId;
    private String goodsName;
    private String picture;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 仓库id
     */
    private Long shopId;
    /**
     * 售价
     */
    private BigDecimal sellingPrice;
    private String barCode;
    private String category;
    private Integer categoryId;
}
