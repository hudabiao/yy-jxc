package io.renren.modules.erp.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ErpSellLogVO {

    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 销售时间
     */
    private Date sellTime;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 实际支付单价
     */
    private BigDecimal actualPaidPrice;

    /**
     * 数量
     */
    private Integer sellCount;

    private Long shopId;

    private String goodsName;
    private String picture;
}
