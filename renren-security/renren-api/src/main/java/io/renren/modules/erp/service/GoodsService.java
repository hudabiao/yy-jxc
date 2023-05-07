package io.renren.modules.erp.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.renren.enums.OperateTypeEnum;
import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.domain.ErpInventoryShop;
import io.renren.modules.erp.dto.InStockDTO;
import io.renren.modules.erp.dto.PostExportDTO;
import io.renren.modules.erp.vo.GoodsCodeVO;
import io.renren.modules.erp.vo.GoodsInfoVO;
import io.renren.modules.oss.cloud.MinioCloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsService {

    @Autowired
    private ErpGoodsService erpGoodsService;

    @Autowired
    private MinioCloudStorageService minioCloudStorageService;

    @Autowired
    private ErpInventoryShopService erpInventoryShopService;

    public void add(InStockDTO instockDTO) {
        erpGoodsService.saveEntity(instockDTO);
    }

    public List<ErpGoods> listAll(Date createDate, int pageNumber) {
        List<ErpGoods> objects;
        int pageSize = 50;
        int offset = (pageNumber - 1) * pageSize;
        String limitStr = "limit " + offset + ","+pageSize;
        if (createDate != null) {
            Date endDate = DateUtils.addDays(createDate, 1);
            objects = erpGoodsService.lambdaQuery()
                    .between(ErpGoods::getCreateTime, createDate, endDate)
                    .orderByDesc(ErpGoods::getId)
                    .last(limitStr).list();
        } else {
            objects = erpGoodsService.lambdaQuery()
                    .orderByDesc(ErpGoods::getId)
                    .last(limitStr).list();
        }
        objects.forEach(obj -> {
            obj.setPicture(minioCloudStorageService.getUrl(obj.getPicture()));
        });
        return objects;
    }

    /**
     * 导出条码
     *
     * @param ids          基地id列表
     * @param outputStream 输出流
     */
    public void exportBarCode(List<Long> ids, OutputStream outputStream) {
        List<ErpGoods> byBaseIds = erpGoodsService.getByIds(ids);
        List<GoodsCodeVO> goodsCodeVOS = byBaseIds.stream().map(entity -> {
            GoodsCodeVO goodsCodeVO = new GoodsCodeVO();
            goodsCodeVO.setGoodsName(entity.getGoodsName());
            goodsCodeVO.setBarCode(entity.getBarCode());
            return goodsCodeVO;
        }).collect(Collectors.toList());
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.write(goodsCodeVOS, true);
        writer.flush(outputStream);
        writer.close();
    }

    /**
     * 导出条码
     *
     * @param date         date
     * @param outputStream 输出流
     */
    public void exportBarCode(String date, OutputStream outputStream) {
        List<ErpGoods> byBaseIds = erpGoodsService.getByDate(date);
        List<GoodsCodeVO> goodsCodeVOS = new ArrayList<>();
        for (ErpGoods goods : byBaseIds) {
            for (int i = 0; i < goods.getAmount(); i++) {
                GoodsCodeVO goodsCodeVO = new GoodsCodeVO();
                goodsCodeVO.setGoodsName(goods.getGoodsName());
                goodsCodeVO.setBarCode(goods.getBarCode());
                goodsCodeVO.setSellingPrice(goods.getSellingPrice().toString());
                goodsCodeVOS.add(goodsCodeVO);
            }
        }
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.write(goodsCodeVOS, true);
        writer.flush(outputStream);
        writer.close();
    }

    /**
     * 导出到xls，上次
     *
     * @param postExportDTOList 后出口dtolist
     * @return {@link String}
     */
    public String postExport(List<PostExportDTO> postExportDTOList){
        if(CollectionUtils.isEmpty(postExportDTOList)){
            throw new RuntimeException("无能为空");
        }
        List<GoodsCodeVO> goodsCodeVOS = new ArrayList<>();
        for (PostExportDTO postExportDTO: postExportDTOList){
            ErpGoods goods = erpGoodsService.getById(postExportDTO.getGoodsId());
            for (int i = 0; i < postExportDTO.getAmount(); i++) {
                GoodsCodeVO goodsCodeVO = new GoodsCodeVO();
                goodsCodeVO.setGoodsName(goods.getGoodsName());
                goodsCodeVO.setBarCode(goods.getBarCode());
                goodsCodeVO.setSellingPrice(goods.getSellingPrice().toString());
                goodsCodeVOS.add(goodsCodeVO);
            }
        }
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
        try(FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream()) {
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.write(goodsCodeVOS, true);
            writer.flush(fastByteArrayOutputStream);
            writer.close();
            try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fastByteArrayOutputStream.toByteArray())) {
                minioCloudStorageService.upload(byteArrayInputStream, fileName, "application/vnd.ms-excel;charset=utf-8");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return fileName;
    }

    public String getExportFileUrl(String fileName){
        return minioCloudStorageService.getUrl(fileName);
    }

    /**
     * 商品信息
     *
     * @param barcode 条形码
     * @return {@link GoodsInfoVO}
     * @throws JsonProcessingException json处理异常
     */
    public GoodsInfoVO goodsInfo(String barcode, int type) {
        GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
        // 商品信息
        ErpGoods goods = erpGoodsService.getByBarcode(barcode);
        Assert.notNull(goods, "商品信息为空！");
        // 库存信息
        if (type == OperateTypeEnum.EXPORT.value()) {
            // 出库
            final ErpInventoryShop inventoryShop = erpInventoryShopService.getByGoodsId(goods.getId());
            goodsInfoVO.setCount(inventoryShop.getAmount());
            goodsInfoVO.setSellingPrice(inventoryShop.getSellingPrice());
        }
        if (type == OperateTypeEnum.IMPORT.value()) {
            // 入库
            goodsInfoVO.setSellingPrice(goods.getSellingPrice());
        }
        // 图片
        if (StringUtils.isNotBlank(goods.getPicture())) {
            String picture = goods.getPicture();
            String url = minioCloudStorageService.getUrl(picture);
            goodsInfoVO.setPicture(url);
        }
        goodsInfoVO.setGoodsName(goods.getGoodsName());
        goodsInfoVO.setGoodsId(goods.getId());
        return goodsInfoVO;
    }

    /**
     * 更新数量
     *
     * @param id     id
     * @param amount 量
     */
    public void updateAmount(Long id, Integer amount){
        erpGoodsService.lambdaUpdate().set(ErpGoods::getAmount, amount).eq(ErpGoods::getId, id);
    }
}
