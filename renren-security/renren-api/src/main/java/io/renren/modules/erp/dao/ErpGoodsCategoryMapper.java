package io.renren.modules.erp.dao;

import io.renren.modules.erp.domain.ErpGoodsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【erp_goods_category(商品分类)】的数据库操作Mapper
* @createDate 2023-02-16 19:18:02
* @Entity io.renren.modules.erp.domain.ErpGoodsCategory
*/
@Mapper
public interface ErpGoodsCategoryMapper extends BaseMapper<ErpGoodsCategory> {

}




