package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.ImmutableList;
import com.happy.domain.Shares;
import com.happy.domain.SharesDaily;
import com.happy.domain.SharesIntermediate;
import com.happy.service.SharesDailyService;
import com.happy.service.SharesIntermediateService;
import com.happy.service.SharesService;
import com.happy.service.StatisticService;
import com.happy.util.Kit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private SharesDailyService sharesDailyService;

    @Autowired
    private SharesIntermediateService sharesIntermediateService;

    @Autowired
    private SharesService sharesService;

    @Override
    public List<JSONObject> dailyList(DailyStopReq req) {

        //  版块的名称不为空
        if(!CollectionUtils.isEmpty(req.getSharesTypeNames()) || !StringUtils.isEmpty(req.getSharesTypeName())) {
            List<SharesIntermediate> list = sharesIntermediateService.lambdaQuery()
                    .in(!CollectionUtils.isEmpty(req.getSharesTypeNames()), SharesIntermediate::getSharesTypeName, req.getSharesTypeNames())
                    .like(StringUtils.isEmpty(req.getSharesTypeName()), SharesIntermediate::getSharesTypeName, req.getSharesTypeName())
                    .list();

            if(CollectionUtils.isEmpty(list)) {
                return ImmutableList.of();
            }

            Set<String> tsCodes = list.stream().map(SharesIntermediate::getTsCode).collect(Collectors.toSet());

            if(!req.buildTsCode(tsCodes)) {
                return ImmutableList.of();
            }
        }

        if(!CollectionUtils.isEmpty(req.getNames()) || !StringUtils.isEmpty(req.getName())) {
            List<Shares> shares = sharesService.lambdaQuery()
                    .in(!CollectionUtils.isEmpty(req.getNames()), Shares::getName, req.getNames())
                    .like(!StringUtils.isEmpty(req.getName()), Shares::getName, req.getName())
                    .list();

            if(CollectionUtils.isEmpty(shares)) {
                return ImmutableList.of();
            }

            Set<String> tsCodes = shares.stream().map(Shares::getTsCode).collect(Collectors.toSet());

            if(!req.buildTsCode(tsCodes)) {
                return ImmutableList.of();
            }

        }

        req.initQuery();

        LambdaQueryChainWrapper<SharesDaily> wrapper = sharesDailyService.lambdaQuery()
                .in(!CollectionUtils.isEmpty(req.getTsCode()), SharesDaily::getTsCode, req.getTsCode())
                .eq(!StringUtils.isEmpty(req.getStop()), SharesDaily::getStop, req.getStop())
                .eq(Objects.nonNull(req.getTradeDate()), SharesDaily::getTradeDate, req.getTradeDate())
                ;


        if(!CollectionUtils.isEmpty(req.getTradeDates()) && req.getTradeDates().size() >= 2) {
            wrapper.between(SharesDaily::getTradeDate,
                    req.getTradeDates().get(0), req.getTradeDates().get(1));
        }

        if(!CollectionUtils.isEmpty(req.getPctChgs()) && req.getPctChgs().size() >= 2) {
            wrapper.between(SharesDaily::getPctChg,
                    req.getPctChgs().get(0), req.getPctChgs().get(1));
        }

        return Kit.toJSONObject(wrapper.list());
    }
}
