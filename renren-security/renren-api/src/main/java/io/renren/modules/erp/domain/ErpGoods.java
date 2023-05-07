package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 基础商品
 * @TableName erp_base_goods
 */
@TableName(value ="erp_goods")
@Data
public class ErpGoods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 销售单价（定价）
     */
    private BigDecimal sellingPrice;

    /**
     * 图片
     */
    private String picture;
    /**
     * 条形码
     */
    private String barCode;

    private Date createTime;

    private Integer amount;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}