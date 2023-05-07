package io.renren.modules.erp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class InStockDTO {
    @NotNull(message = "必须选择：分类")
    private Integer categoryId;
    @NotNull(message = "必须填写：数量")
    private Integer count;
    @NotBlank(message = "必须填写：名称")
    private String goodsName;
    private String picture;
    @NotNull(message = "必须填写：进价")
    private BigDecimal purchasePrice;
    @NotNull(message = "必须填写：售价")
    private BigDecimal sellingPrice;
}
