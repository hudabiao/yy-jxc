package io.renren.modules.erp.controller;

import io.renren.common.utils.Result;
import io.renren.modules.erp.bo.SellSummaryBO;
import io.renren.modules.erp.service.SummaryService;
import io.renren.modules.erp.vo.AllSellSummaryVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RequestMapping("/summary")
@RestController
public class SummaryController {

    private SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/today")
    public Result<SellSummaryBO> todaySummary() throws ParseException {
        final LocalDate now = LocalDate.now();
        final String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final SellSummaryBO summary = summaryService.summary(format);
        return Result.ok(summary);
    }

    @GetMapping("/all")
    public Result<List<AllSellSummaryVO>> allSummary(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                     @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = DateUtils.addDays(endDate, 1);
        final List<AllSellSummaryVO> allSellSummaryVOS = summaryService.allSummary(startDate, endDate);
        return Result.ok(allSellSummaryVOS);
    }

}
