package io.renren.modules.erp.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.modules.erp.dto.GoodsSellDto;
import io.renren.modules.erp.dto.ShopGoodsDTO;
import io.renren.modules.erp.dto.ShopImportDTO;
import io.renren.modules.erp.dto.TransferGoodsDTO;
import io.renren.modules.erp.service.ShopInventoryService;
import io.renren.modules.erp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 店铺库存
 *
 * @author hudaibiao
 * @date 2023/02/28
 */
@RequestMapping("/shop-inventory")
@RestController
public class ShopInventoryController {

    @Autowired
    private ShopInventoryService shopInventoryService;

    @LogOperation("店铺商品-调拨")
    @PostMapping("/transfer")
    public Result<Void> transfer(@RequestBody List<TransferGoodsDTO> transferGoodsDTOS) {
        shopInventoryService.transferGoods(transferGoodsDTOS);
        return Result.ok();
    }

    @LogOperation("店铺商品-调拨")
    @GetMapping("/my-transfer")
    public Result<List<TransferShopVO>> myTransfer(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        final List<TransferShopVO> thisTransferList = shopInventoryService.getThisTransferList(categoryId);
        return Result.ok(thisTransferList);
    }

    @LogOperation("店铺商品-入库")
    @PostMapping("/import")
    public Result<List<InventoryVO>> importGoods(@RequestBody ShopGoodsDTO shopGoodsDTO) {
        shopInventoryService.importGoods(shopGoodsDTO);
        return Result.ok();
    }

    @LogOperation("店铺商品-调拨入库")
    @GetMapping("/import-transfer/{transferId}")
    public Result<List<InventoryVO>> importTransfer(@PathVariable Long transferId) {
        shopInventoryService.importTransfer(transferId);
        return Result.ok();
    }

    @LogOperation("店铺商品-出库（零售）")
    @PostMapping("/sell")
    public Result<GoodsInfoVO> sell(@RequestBody GoodsSellDto goodsSellDto) {
        final boolean b = shopInventoryService.sell(goodsSellDto);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail("出现异常");
        }
    }

    @LogOperation("店铺商品-销售回滚")
    @GetMapping("/sell/rollback/{id}")
    public Result<GoodsInfoVO> sellRollback(@PathVariable long id) {
        final boolean b = shopInventoryService.rollbackSell(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail("出现异常");
        }
    }

    @LogOperation("店铺商品-销售记录")
    @GetMapping("/sell/record")
    public Result<List<ErpSellLogVO>> sellRecord(@RequestParam Date recordDate) {
        List<ErpSellLogVO> sellRecord = shopInventoryService.getSellRecord(recordDate);
        return Result.ok(sellRecord);
    }

    @LogOperation("店铺商品-库存记录")
    @GetMapping("/list")
    public Result<List<InventoryShopVO>> listResult(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        List<InventoryShopVO> inventoryShopVOS = shopInventoryService.getShopInventory(categoryId);
        return Result.ok(inventoryShopVOS);
    }
}
