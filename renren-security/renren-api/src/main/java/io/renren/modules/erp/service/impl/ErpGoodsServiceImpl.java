package io.renren.modules.erp.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.erp.CodeUtil;
import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.dto.InStockDTO;
import io.renren.modules.erp.service.ErpGoodsService;
import io.renren.modules.erp.dao.ErpGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【erp_base_goods(基础商品)】的数据库操作Service实现
* @createDate 2023-02-16 19:18:02
*/
@Slf4j
@Service
public class ErpGoodsServiceImpl extends ServiceImpl<ErpGoodsMapper, ErpGoods>
    implements ErpGoodsService {

    @Autowired
    private CodeUtil codeUtil;

    @Override
    public Long saveEntity(InStockDTO instockDTO) {
        ErpGoods erpGoods = new ErpGoods();
        erpGoods.setAmount(instockDTO.getCount());
        erpGoods.setGoodsName(instockDTO.getGoodsName());
        erpGoods.setCategoryId(instockDTO.getCategoryId());
        erpGoods.setPurchasePrice(instockDTO.getPurchasePrice());
        erpGoods.setSellingPrice(instockDTO.getSellingPrice());
        erpGoods.setPicture(instockDTO.getPicture());
        erpGoods.setBarCode(codeUtil.getCode());
        erpGoods.setCreateTime(new Date());
        this.save(erpGoods);
        return erpGoods.getId();
    }

    @Override
    public List<ErpGoods> getByIds(List<Long> ids) {
        return lambdaQuery().in(ErpGoods::getId, ids).list();
    }

    @Override
    public List<ErpGoods> getByDate(String date) {
        if (StringUtils.isBlank(date)) {
            return list();
        }
        Date startDate = null;
        try {
            startDate = DateUtils.parseDate(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        assert startDate != null;
        Date endDate = DateUtils.addDays(startDate, 1);
        return lambdaQuery().between(ErpGoods::getCreateTime, startDate, endDate).list();
    }

    @Override
    public ErpGoods getByBarcode(String barcode) {
        return lambdaQuery().eq(ErpGoods::getBarCode, barcode).one();
    }
}




