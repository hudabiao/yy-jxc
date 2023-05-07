package io.renren.modules.erp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferGoodsDTO {
    /**
     * 条形码
     */
    private Long goodsId;

    private Long shopId;

    /**
     * 数量
     */
    private Integer amount;

}
