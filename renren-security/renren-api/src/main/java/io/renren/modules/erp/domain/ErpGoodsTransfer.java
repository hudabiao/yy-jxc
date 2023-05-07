package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName erp_goods_transfer
 */
@TableName(value ="erp_goods_transfer")
@Data
public class ErpGoodsTransfer implements Serializable {
    /**
     * 
     */
    private Long goodsId;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Integer amount;

    /**
     * 
     */
    private Long shopId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}