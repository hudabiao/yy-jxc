package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 商品分类
 * @TableName erp_goods_category
 */
@TableName(value ="erp_goods_category")
@Data
public class ErpGoodsCategory implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String category;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}