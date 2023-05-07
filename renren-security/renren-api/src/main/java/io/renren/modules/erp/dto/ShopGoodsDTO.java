package io.renren.modules.erp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ShopGoodsDTO {
    /**
     * 条形码
     */
    private String barCode;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;
}
