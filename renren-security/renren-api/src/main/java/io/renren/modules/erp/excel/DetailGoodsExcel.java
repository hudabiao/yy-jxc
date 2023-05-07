package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 细分商品
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class DetailGoodsExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "基本商品id")
    private Long baseGoodsId;
    @Excel(name = "属性")
    private String properties;

}