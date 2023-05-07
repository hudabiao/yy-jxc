package io.renren.modules.erp.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellSummaryBO {
    /**
     * 销售数量
     */
    private Integer sellAmount;
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    private BigDecimal totalProfit;
    private BigDecimal wechatIncome;
    private BigDecimal alipayIncome;
    private BigDecimal cashIncome;
    private BigDecimal otherIncome;

}
