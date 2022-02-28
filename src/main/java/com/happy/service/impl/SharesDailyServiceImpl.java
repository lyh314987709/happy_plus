package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.happy.ask.AskMoneyService;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDailyDao;
import com.happy.domain.Shares;
import com.happy.domain.SharesDaily;
import com.happy.en.AskMoneyReqEnum;
import com.happy.net.ClientService;
import com.happy.service.SharesDailyService;
import com.happy.service.SharesService;
import com.happy.util.Constant;
import com.happy.util.Kit;
import com.happy.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SharesDailyServiceImpl extends ServiceImpl<SharesDailyDao, SharesDaily> implements SharesDailyService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private AskMoneyService askMoneyService;

    @Autowired
    private SharesService sharesService;

    @Override
    public List<SharesDaily> synchronization(SharesDailyReq req) {
        List<Map<String, String>> list = clientService.post(ClientService.Api.DAILY, JSONObject.parseObject(JSONObject.toJSONString(req)));

        List<SharesDaily> sharesDailies = ConvertUtil.convertLike(list, SharesDaily.class);

        Map<String, Shares> map = sharesService.map(ImmutableSet.of());

        sharesDailies = sharesDailies.stream()
                .map(item -> item.build(map.get(item.getTsCode())))
                .collect(Collectors.toList());

        Lists.partition(sharesDailies, 200).forEach(item -> saveOrUpdateBatch(item));
        return sharesDailies;
    }

    @Override
    public void isStop(LocalDateTime time) {
        JSONObject json = new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time), "涨停跌停"));
        simple(ClientService.Api.ASK_MONEY_STRATEGY, json, AskMoneyReqEnum.STOP,
                (e) -> lambdaQuery().in(SharesDaily::getTsCode, e)
                .eq(SharesDaily::getTradeDate, time.toLocalDate())
                .list());
    }

    private void simple(ClientService.Api api, JSONObject json, AskMoneyReqEnum askMoneyReqEnum, Function<Set<String>, List<SharesDaily>> function) {

        List<Map<String, String>> stopList = askMoneyService.defGet(api, askMoneyReqEnum, json);

        List<JSONObject> result = askMoneyReqEnum.result(stopList);

        List<SharesDaily> sharesDailies = Kit.parseArray(result, SharesDaily.class);

        Map<String, SharesDaily> sharesDailyMap = Maps.uniqueIndex(sharesDailies, SharesDaily::getTsCode);

        List<SharesDaily> list = function.apply(sharesDailyMap.keySet());

        List<SharesDaily> updateList = list.stream().map(item -> {
            SharesDaily sharesDaily = sharesDailyMap.get(item.getTsCode());
            sharesDaily.setId(item.getId());
            return sharesDaily;
        }).collect(Collectors.toList());

        Lists.partition(updateList, 200).forEach(item -> taskExecutor.execute(() -> saveOrUpdateBatch(item)));
    }

    @Override
    public void isStop() {
        isStop(LocalDateTime.now());
    }

    @Override
    public void onceStop(LocalDateTime time) {
        JSONObject json = new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time), "曾涨停"));
        simple(ClientService.Api.ASK_MONEY_STRATEGY, json, AskMoneyReqEnum.STOP,
                (e) -> lambdaQuery().in(SharesDaily::getTsCode, e)
                        .eq(SharesDaily::getTradeDate, time.toLocalDate())
                        .list());
    }

    @Override
    public void onceStop() {
      onceStop(LocalDateTime.now());
    }
}
