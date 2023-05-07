package io.renren.modules.erp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryBaseBO {
    /**
     * 商品名称
     */
    private String goodsName;

    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 图片
     */
    private String picture;

    private Integer amount;

    private String barCode;

    private Date createTime;
}
