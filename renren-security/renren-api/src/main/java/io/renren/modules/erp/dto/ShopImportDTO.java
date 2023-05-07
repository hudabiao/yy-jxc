package io.renren.modules.erp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopImportDTO {
    /**
     * 条形码
     */
    private Long goodsId;

    /**
     * 数量
     */
    private Integer amount;
}
