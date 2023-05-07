package io.renren.modules.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.domain.ErpInventoryShop;
import io.renren.modules.erp.dto.ShopGoodsDTO;
import io.renren.modules.erp.service.ErpGoodsCategoryService;
import io.renren.modules.erp.service.ErpGoodsService;
import io.renren.modules.erp.service.ErpInventoryShopService;
import io.renren.modules.erp.dao.ErpInventoryShopMapper;
import io.renren.modules.security.user.SecurityUser;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【erp_inventory_shop(库存)】的数据库操作Service实现
 * @createDate 2023-03-14 16:25:04
 */
@Service
public class ErpInventoryShopServiceImpl extends ServiceImpl<ErpInventoryShopMapper, ErpInventoryShop>
        implements ErpInventoryShopService {


    @Override
    public void saveByDto(ShopGoodsDTO shopGoodsDTO, ErpGoods goods) {
        BigDecimal sellingPrice = null;
        if (shopGoodsDTO.getSellingPrice() != null) {
            sellingPrice = shopGoodsDTO.getSellingPrice();
        } else {
            sellingPrice = goods.getSellingPrice();
        }
        // 保存-店铺库存
        final Long deptId = SecurityUser.getDeptId();
        final ErpInventoryShop one = lambdaQuery().eq(ErpInventoryShop::getGoodsId, goods.getId()).eq(ErpInventoryShop::getShopId, deptId).one();
        if (one != null) {
            one.setAmount(shopGoodsDTO.getAmount() + one.getAmount());
            one.setSellingPrice(sellingPrice);
            this.updateById(one);
            return;
        }
        ErpInventoryShop erpInventoryShop = new ErpInventoryShop();
        erpInventoryShop.setGoodsId(goods.getId());
        erpInventoryShop.setCategoryId(goods.getCategoryId());
        erpInventoryShop.setAmount(shopGoodsDTO.getAmount());
        erpInventoryShop.setShopId(deptId);
        erpInventoryShop.setSellingPrice(sellingPrice);
        this.save(erpInventoryShop);
    }

    @Override
    public ErpInventoryShop getByGoodsId(Long goodsId) {
        final Long deptId = SecurityUser.getDeptId();
        final ErpInventoryShop one = lambdaQuery().eq(ErpInventoryShop::getGoodsId, goodsId).eq(ErpInventoryShop::getShopId, deptId).one();
        Assert.notNull(one, "商品库存信息不存在！");
        return one;
    }

    @Override
    public void updateAmount(Long id, Integer amount) {
        lambdaUpdate().set(ErpInventoryShop::getAmount, amount).eq(ErpInventoryShop::getId, id).update();
    }

    @Override
    public List<ErpInventoryShop> getList(Integer categoryId) {
        final Long deptId = SecurityUser.getDeptId();
        return lambdaQuery().eq(ErpInventoryShop::getShopId, deptId).eq(categoryId != null, ErpInventoryShop::getCategoryId, categoryId).list();
    }
}




