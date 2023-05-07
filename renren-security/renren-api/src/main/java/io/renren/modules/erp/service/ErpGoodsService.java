package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.erp.dto.InStockDTO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_base_goods(基础商品)】的数据库操作Service
* @createDate 2023-02-16 19:18:02
*/
public interface ErpGoodsService extends IService<ErpGoods> {
    Long saveEntity(InStockDTO instockDTO);
    List<ErpGoods> getByIds(List<Long> ids);
    List<ErpGoods> getByDate(String date);
    ErpGoods getByBarcode(String barcode);
}
