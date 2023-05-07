package io.renren.modules.erp.controller;

import io.renren.common.utils.Result;
import io.renren.modules.erp.domain.ErpGoodsCategory;
import io.renren.modules.erp.service.ErpGoodsCategoryService;
import io.renren.modules.erp.service.GoodsService;
import io.renren.modules.erp.vo.GoodsInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private ErpGoodsCategoryService erpGoodsCategoryService;

    /**
     * 导出条码
     *
     * @param baseIdList 基地id列表
     * @param response   响应
     */
    @GetMapping("/list")
    public Result<List<ErpGoodsCategory>> list() {
        List<ErpGoodsCategory> erpGoodsCategories = erpGoodsCategoryService.listAll();
        return Result.ok(erpGoodsCategories);
    }
}
