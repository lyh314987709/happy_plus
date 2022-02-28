package com.happy.controller;


import com.happy.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/statistical")
public class StatisticalController {

    @Autowired
    private StatisticService statisticService;

    @PostMapping("/daily/list")
    public ApiResult queryAccountAmount(@RequestBody  StatisticService.DailyStopReq req) {
        return ApiResult.success(statisticService.dailyList(req));

    }

}
