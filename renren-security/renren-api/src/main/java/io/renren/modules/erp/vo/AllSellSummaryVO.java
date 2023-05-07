package io.renren.modules.erp.vo;

import io.renren.modules.erp.bo.SellSummaryBO;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class AllSellSummaryVO extends SellSummaryBO {
    private String deptName;
    private Long deptId;
}
