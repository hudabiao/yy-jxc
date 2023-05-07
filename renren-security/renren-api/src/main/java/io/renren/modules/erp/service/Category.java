package io.renren.modules.erp.service;

import io.renren.modules.erp.domain.ErpGoodsCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Category {

    private static Map<Long, String> categoryMap;

    public static void set(List<ErpGoodsCategory> list){
        categoryMap = list.stream().collect(Collectors.toMap(ErpGoodsCategory::getId, ErpGoodsCategory::getCategory));
    }

    public static Map<Long, String> get(){
        return categoryMap;
    }
}
