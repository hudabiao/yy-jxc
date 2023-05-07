package io.renren.modules.erp.vo;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryVO {
    private Long id;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 分类id
     */
    private String category;

    /**
     * 图片
     */
    private String picture;

    private String amount;

    private String barCode;

    private Date createTime;
}
