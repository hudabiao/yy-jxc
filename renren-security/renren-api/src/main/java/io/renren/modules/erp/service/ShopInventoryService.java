package io.renren.modules.erp.service;

import io.renren.enums.PaymentMethodEnum;
import io.renren.modules.erp.domain.*;
import io.renren.modules.erp.dto.GoodsSellDto;
import io.renren.modules.erp.dto.ShopGoodsDTO;
import io.renren.modules.erp.dto.ShopImportDTO;
import io.renren.modules.erp.dto.TransferGoodsDTO;
import io.renren.modules.erp.vo.ErpSellLogVO;
import io.renren.modules.erp.vo.InventoryShopVO;
import io.renren.modules.erp.vo.TransferShopVO;
import io.renren.modules.oss.cloud.MinioCloudStorageService;
import io.renren.modules.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 店铺库存
 *
 * @author hudaibiao
 * @date 2023/02/03
 */
@Slf4j
@Service
public class ShopInventoryService {

    @Autowired
    private ErpInventoryShopService erpInventoryShopService;

    @Autowired
    private ErpGoodsService erpGoodsService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ErpSellLogService erpSellLogService;

    @Autowired
    private MinioCloudStorageService minioCloudStorageService;

    @Autowired
    private ErpGoodsCategoryService erpGoodsCategoryService;

    @Autowired
    private ErpGoodsTransferService erpGoodsTransferService;

    /**
     * 店铺商品-调拨
     *
     * @param transferGoodsDTOS 店商品dto
     */
    public void transferGoods(List<TransferGoodsDTO> transferGoodsDTOS) {
        final List<ErpGoodsTransfer> collect = transferGoodsDTOS.stream().map(transfer -> {
            final ErpGoodsTransfer erpGoodsTransfer = new ErpGoodsTransfer();
            erpGoodsTransfer.setGoodsId(transfer.getGoodsId());
            erpGoodsTransfer.setAmount(transfer.getAmount());
            erpGoodsTransfer.setShopId(transfer.getShopId());
            return erpGoodsTransfer;
        }).collect(Collectors.toList());
        erpGoodsTransferService.saveBatch(collect);
    }

    public List<TransferShopVO> getThisTransferList(Integer categoryId) {
        final Long deptId = SecurityUser.getDeptId();
        final List<ErpGoodsTransfer> list = erpGoodsTransferService.lambdaQuery().eq(ErpGoodsTransfer::getShopId, deptId).list();
        List<TransferShopVO> inventoryShopVOS = list.stream().map(transfer -> {
            final ErpGoods goods = erpGoodsService.getById(transfer.getGoodsId());
            final TransferShopVO inventoryShopVO = new TransferShopVO();
            inventoryShopVO.setId(transfer.getId());
            inventoryShopVO.setGoodsId(transfer.getGoodsId());
            inventoryShopVO.setGoodsName(goods.getGoodsName());
            inventoryShopVO.setPicture(minioCloudStorageService.getUrl(goods.getPicture()));
            inventoryShopVO.setAmount(transfer.getAmount());
            inventoryShopVO.setShopId(transfer.getShopId());
            inventoryShopVO.setSellingPrice(goods.getSellingPrice());
            inventoryShopVO.setBarCode(goods.getBarCode());
            inventoryShopVO.setCategoryId(goods.getCategoryId());
            return inventoryShopVO;
        }).collect(Collectors.toList());
        if (categoryId != null) {
            inventoryShopVOS = inventoryShopVOS.stream()
                    .filter(inventoryShopVO -> Objects.equals(categoryId, inventoryShopVO.getCategoryId()))
                    .collect(Collectors.toList());
        }
        return inventoryShopVOS;
    }

    /**
     * 店铺商品-入库
     *
     * @param shopGoodsDTO 店商品dto
     */
    public void importGoods(ShopGoodsDTO shopGoodsDTO) {
        final ErpGoods goods = erpGoodsService.getByBarcode(shopGoodsDTO.getBarCode());
        erpInventoryShopService.saveByDto(shopGoodsDTO, goods);
    }

    public void importTransfer(Long transferId) {
        final ErpGoodsTransfer transfer = erpGoodsTransferService.getById(transferId);
        final ErpGoods goods = erpGoodsService.getById(transfer.getGoodsId());
        final ShopGoodsDTO shopGoodsDTO = new ShopGoodsDTO();
        shopGoodsDTO.setAmount(transfer.getAmount());
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            erpInventoryShopService.saveByDto(shopGoodsDTO, goods);
            erpGoodsTransferService.removeById(transferId);
            transactionManager.commit(transactionStatus);
        } catch (Exception e){
            log.error("出库异常", e);
            transactionManager.rollback(transactionStatus);
        }
    }

    public boolean rollbackSell(Long id){
        ErpSellLog erpSellLog = erpSellLogService.getById(id);
        ErpInventoryShop inventoryShop = erpInventoryShopService.getByGoodsId(erpSellLog.getGoodsId());
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            // 库存-回填
            erpInventoryShopService.updateAmount(inventoryShop.getId(), inventoryShop.getAmount() + erpSellLog.getSellCount());
            // 删除-记录
            erpSellLogService.removeById(id);
            transactionManager.commit(transactionStatus);
            return true;
        } catch (Exception e){
            log.error("回滚销售异常", e);
            transactionManager.rollback(transactionStatus);
            return false;
        }
    }

    /**
     * 出售
     *
     * @param goodsSellDto 商品出售dto
     * @return boolean
     */
    public boolean sell(GoodsSellDto goodsSellDto) {
        String barcode = goodsSellDto.getBarCode();
        Integer sellCount = goodsSellDto.getSellCount();
        if (sellCount <= 0) {
            throw new RuntimeException("出库数量错误！");
        }
        // 基本信息
        ErpGoods goods = erpGoodsService.getByBarcode(barcode);
        final Long id = goods.getId();
        final ErpInventoryShop inventoryShop = erpInventoryShopService.getByGoodsId(goods.getId());
        if (sellCount > inventoryShop.getAmount()) {
            throw new RuntimeException("出库数量错误！");
        }
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            // 保存：出库记录
            ErpSellLog erpSellLog = new ErpSellLog();
            erpSellLog.setGoodsId(id);
            erpSellLog.setSellTime(new Date());
            erpSellLog.setPaymentMethod(goodsSellDto.getPaymentMethod());
            erpSellLog.setPurchasePrice(goods.getPurchasePrice());
            erpSellLog.setActualPaidPrice(goodsSellDto.getSellingPrice());
            erpSellLog.setSellCount(sellCount);
            erpSellLog.setShopId(SecurityUser.getDeptId());
            erpSellLogService.save(erpSellLog);
            // 扣减库存
            erpInventoryShopService.updateAmount(inventoryShop.getId(), inventoryShop.getAmount() - sellCount);
            // 提交
            transactionManager.commit(transactionStatus);
            return true;
        } catch (Exception e) {
            log.error("出库异常", e);
            transactionManager.rollback(transactionStatus);
            return false;
        }
    }

    /**
     * 获得销售记录
     *
     * @param startDate 开始日期
     * @return {@link List}<{@link ErpSellLogVO}>
     */
    public List<ErpSellLogVO> getSellRecord(Date startDate) {
        Date endDate = DateUtils.addDays(startDate, 1);
        List<ErpSellLog> list = erpSellLogService.lambdaQuery().eq(ErpSellLog::getShopId, SecurityUser.getDeptId())
                .gt(ErpSellLog::getSellTime, startDate)
                .lt(ErpSellLog::getSellTime, endDate).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(log -> {
            ErpSellLogVO erpSellLogVO = new ErpSellLogVO();
            ErpGoods goods = erpGoodsService.getById(log.getGoodsId());
            BeanUtils.copyProperties(log, erpSellLogVO);
            erpSellLogVO.setPaymentMethod(PaymentMethodEnum.getByValue(log.getPaymentMethod()));
            erpSellLogVO.setId(log.getId());
            if (goods != null) {
                String goodsName = goods.getGoodsName();
                String picture = goods.getPicture();
                String url = minioCloudStorageService.getUrl(picture);
                erpSellLogVO.setGoodsName(goodsName);
                erpSellLogVO.setPicture(url);
            }
            return erpSellLogVO;
        }).collect(Collectors.toList());
    }

    public List<InventoryShopVO> getShopInventory(Integer categoryId) {
        final List<ErpGoodsCategory> erpGoodsCategories = erpGoodsCategoryService.listAll();
        final Map<Long, String> categoryMap = erpGoodsCategories.stream().collect(Collectors.toMap(ErpGoodsCategory::getId, ErpGoodsCategory::getCategory));
        final List<ErpInventoryShop> list = erpInventoryShopService.getList(categoryId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(inventoryShop -> {
            InventoryShopVO inventoryShopVO = new InventoryShopVO();
            ErpGoods goods = erpGoodsService.getById(inventoryShop.getGoodsId());
            BeanUtils.copyProperties(inventoryShop, inventoryShopVO);
            if (goods != null) {
                String goodsName = goods.getGoodsName();
                String picture = goods.getPicture();
                String url = minioCloudStorageService.getUrl(picture);
                inventoryShopVO.setGoodsName(goodsName);
                inventoryShopVO.setPicture(url);
                inventoryShopVO.setBarCode(goods.getBarCode());
                final long id = Optional.ofNullable(inventoryShop.getCategoryId()).map(Integer::longValue).orElse(0L);
                inventoryShopVO.setCategory(categoryMap.get(id));
            }
            return inventoryShopVO;
        }).collect(Collectors.toList());
    }
}
