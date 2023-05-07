package io.renren.modules.erp.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 销售记录
 * @TableName erp_sell_log
 */
@TableName(value ="erp_sell_log")
@Data
public class ErpSellLog implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 销售时间
     */
    private Date sellTime;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 实际支付单价
     */
    private BigDecimal actualPaidPrice;

    /**
     * 数量
     */
    private Integer sellCount;

    private Long shopId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}