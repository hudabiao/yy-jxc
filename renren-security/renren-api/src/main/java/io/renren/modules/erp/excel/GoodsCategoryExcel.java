package io.renren.modules.erp.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 商品分类
 *
 * @author hudaibiao 873078650@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
public class GoodsCategoryExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "分类名称")
    private String category;

}