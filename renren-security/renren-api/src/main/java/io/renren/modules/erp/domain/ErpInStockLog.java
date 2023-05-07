package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 入库记录
 * @TableName erp_in_stock_log
 */
@TableName(value ="erp_in_stock_log")
@Data
public class ErpInStockLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 入库时间
     */
    private Date inStockTime;

    /**
     * 入库数量
     */
    private Integer inStockCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}