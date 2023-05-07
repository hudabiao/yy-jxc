package io.renren.modules.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.erp.domain.ErpGoodsCategory;
import io.renren.modules.erp.service.ErpGoodsCategoryService;
import io.renren.modules.erp.dao.ErpGoodsCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_goods_category(商品分类)】的数据库操作Service实现
* @createDate 2023-02-16 19:18:02
*/
@Service
public class ErpGoodsCategoryServiceImpl extends ServiceImpl<ErpGoodsCategoryMapper, ErpGoodsCategory>
    implements ErpGoodsCategoryService{

    @Override
    public List<ErpGoodsCategory> listAll() {
        return this.list();
    }
}




