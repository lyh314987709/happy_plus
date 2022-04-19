package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.happy.ask.AskMoneyService;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDailyDao;
import com.happy.domain.Shares;
import com.happy.domain.SharesDaily;
import com.happy.en.AskMoneyReqEnum;
import com.happy.event.domian.LocalEvent;
import com.happy.net.ClientService;
import com.happy.service.SharesDailyService;
import com.happy.service.SharesService;
import com.happy.util.JSONObjectUtil;
import com.happy.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<SharesDaily> synchronization(SharesDailyReq req) {

        if(!StringUtils.isEmpty(req.getTrade_date())) {
            // 删除历史数据
            this.baseMapper.delete(Wrappers.<SharesDaily>lambdaQuery().eq(SharesDaily::getTradeDate, req.getTrade_date()));
        }

        List<Map<String, String>> list = clientService.post(ClientService.Api.DAILY, JSONObjectUtil.toJSONObject(req));

        List<SharesDaily> sharesDailies = ConvertUtil.convertLike(list, SharesDaily.class);

        Map<String, Shares> map = sharesService.map(ImmutableSet.of());

        sharesDailies = sharesDailies.stream()
                .map(item -> item.build(map.get(item.getTsCode())))
                .collect(Collectors.toList());


        List<CompletableFuture<Boolean>> completableFutures = Lists.partition(sharesDailies, 1000)
                .stream().map(item -> CompletableFuture.supplyAsync(() -> saveOrUpdateBatch(item)))
                .collect(Collectors.toList());

        while (!completableFutures.stream().map(item -> {
            try {
                return item.get();
            } catch (Exception e) {
                return Boolean.TRUE;
            }
        }).allMatch(item -> item)) {
            try {
                log.info("线程休眠1s钟");
                Thread.sleep(1000l);
            } catch (Exception e) {
                log.error("线程休眠1s钟，message:{}", e.getMessage(), e);
            }
        }

        eventPublisher.publishEvent(new LocalEvent(req));
        return ImmutableList.of();
    }

    private void simple(ClientService.Api api, JSONObject json, AskMoneyReqEnum askMoneyReqEnum, Function<Set<String>, List<SharesDaily>> function) {

        List<Map<String, String>> stopList = askMoneyService.defGet(api, askMoneyReqEnum, json);


        List<JSONObject> result = askMoneyReqEnum.result(stopList);

        List<SharesDaily> sharesDailies = JSONObjectUtil.parseArray(result, SharesDaily.class);

        Map<String, SharesDaily> sharesDailyMap = Maps.uniqueIndex(sharesDailies, SharesDaily::getTsCode);

        List<SharesDaily> list = function.apply(sharesDailyMap.keySet());

        List<SharesDaily> updateList = list.stream().map(item -> {
            SharesDaily sharesDaily = sharesDailyMap.get(item.getTsCode());
            sharesDaily.setId(item.getId());
            return sharesDaily;
        }).collect(Collectors.toList());

        Lists.partition(updateList, 1000).forEach(item -> taskExecutor.execute(() -> updateBatchById(item)));
    }

    private void simplePredicate(ClientService.Api api, JSONObject json,
                        AskMoneyReqEnum askMoneyReqEnum,
                        Predicate<List<SharesDaily>> predicate) {

        List<Map<String, String>> stopList = askMoneyService.defGet(api, askMoneyReqEnum, json);

        log.info("接口返回的原始数据：{}", JSONObject.toJSONString(stopList));

        List<JSONObject> result = askMoneyReqEnum.result(stopList);

        List<SharesDaily> sharesDailies = JSONObjectUtil.parseArray(result, SharesDaily.class);

        log.info("接口组装后的数据：{}", JSONObject.toJSONString(sharesDailies));

        predicate.test(sharesDailies);
    }


    @Override
    public void onceStop(LocalDateTime time, JSONObject json) {
         simple(ClientService.Api.ASK_MONEY_STRATEGY, json, AskMoneyReqEnum.STOP,
                (e) -> lambdaQuery().in(SharesDaily::getTsCode, e)
                        .eq(SharesDaily::getTradeDate, LocalDateTimeUtil.toString(time))
                        .list());
    }

    @Override
    public void handler(LocalDateTime time, JSONObject json, Predicate<List<SharesDaily>> predicate) {
        simplePredicate(ClientService.Api.ASK_MONEY_STRATEGY, json, AskMoneyReqEnum.STOP, predicate);
    }
}
