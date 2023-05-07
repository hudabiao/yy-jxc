package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.domain.ErpInventoryShop;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.erp.dto.ShopGoodsDTO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_inventory_shop(库存)】的数据库操作Service
* @createDate 2023-03-14 16:25:04
*/
public interface ErpInventoryShopService extends IService<ErpInventoryShop> {
    void saveByDto(ShopGoodsDTO shopGoodsDTO, ErpGoods goods);
    ErpInventoryShop getByGoodsId(Long goodsId);
    void updateAmount(Long id, Integer amount);
    List<ErpInventoryShop> getList(Integer categoryId);
}
