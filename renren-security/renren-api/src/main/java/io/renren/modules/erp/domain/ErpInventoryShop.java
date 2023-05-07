package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 库存
 * @TableName erp_inventory_shop
 */
@TableName(value ="erp_inventory_shop")
@Data
public class ErpInventoryShop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    private Integer categoryId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}