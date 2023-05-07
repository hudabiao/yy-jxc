package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpGoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_goods_category(商品分类)】的数据库操作Service
* @createDate 2023-02-16 19:18:02
*/
public interface ErpGoodsCategoryService extends IService<ErpGoodsCategory> {

    List<ErpGoodsCategory> listAll();
}
