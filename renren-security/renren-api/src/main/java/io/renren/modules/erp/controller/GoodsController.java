package io.renren.modules.erp.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.modules.erp.domain.ErpGoods;
import io.renren.modules.erp.dto.AmountUpdateDTO;
import io.renren.modules.erp.dto.InStockDTO;
import io.renren.modules.erp.dto.PostExportDTO;
import io.renren.modules.erp.service.GoodsService;
import io.renren.modules.erp.vo.GoodsInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    @LogOperation("商品-采购入库")
    public Result<Void> save(@RequestBody @Validated InStockDTO dto){
        goodsService.add(dto);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<List<ErpGoods>> list(
            @RequestParam(value = "createDate", required = false) Date createDate,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber){
        if(pageNumber==null){
            pageNumber = 1;
        }
        final List<ErpGoods> erpGoods = goodsService.listAll(createDate, pageNumber);
        return Result.ok(erpGoods);
    }


    /**
     * 设置数量
     *
     * @param amountUpdateDTO 量更新dto
     * @return {@link Result}<{@link List}<{@link ErpGoods}>>
     */
    @PutMapping("/set_amount")
    public Result<List<ErpGoods>> setAmount(@RequestBody AmountUpdateDTO amountUpdateDTO){
        goodsService.updateAmount(amountUpdateDTO.getId(), amountUpdateDTO.getAmount());
        return Result.ok();
    }

    /**
     * 导出条码
     *
     * @param baseIdList 基地id列表
     * @param response   响应
     */
    @GetMapping("/export/code")
    public void export(@RequestParam("date") String date, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            goodsService.exportBarCode(date, outputStream);
        } catch (Exception e) {
            throw new RuntimeException("出错！");
        }
    }

    /**
     * 导出条码
     *
     * @param list list
     */
    @PostMapping("/export/post")
    public Result<String> postExport(@RequestBody List<PostExportDTO> list) {
        String fileName = goodsService.postExport(list);
        return Result.ok(fileName);
    }

    /**
     * 导出条码
     *
     * @param fileName fileName
     */
    @GetMapping("/getExportFileUrl/{fileName}")
    public Result<String> getExportFileUrl(@PathVariable String fileName) {
        final String exportFileUrl = goodsService.getExportFileUrl(fileName);
        return Result.ok(exportFileUrl);
    }

    /**
     * 商品信息
     *
     * @param barCode 条形码
     * @return {@link Result}<{@link GoodsInfoVO}>
     */
    @GetMapping("/info/{barCode}/{type}")
    public Result<GoodsInfoVO> goodsInfo(@PathVariable String barCode, @PathVariable int type){
        GoodsInfoVO goodsInfoVO = goodsService.goodsInfo(barCode, type);
        return Result.ok(goodsInfoVO);
    }
}
