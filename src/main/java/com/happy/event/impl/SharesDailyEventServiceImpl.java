package com.happy.event.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.happy.domain.SharesDaily;
import com.happy.event.domian.LocalEvent;
import com.happy.service.SharesDailyService;
import com.happy.util.Constant;
import com.happy.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SharesDailyEventServiceImpl {

    @Autowired
    private SharesDailyService sharesDailyService;

    @EventListener
    @Order(value = 0)
    @Async
    public void sharesDailyStopEvent(LocalEvent localEvent) {

        log.debug("处理涨跌停数据， localEvent：{}", JSONObject.toJSONString(localEvent));

        LocalDateTime time = buildLocalDateTime(localEvent);

        if(Objects.isNull(time)) {
            return ;
        }

        //  处理曾涨跌停数据
        sharesDailyService.handler(time, new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time), "涨停跌停")), y -> {
            //  代码
            Set<String> keys = y.stream().map(SharesDaily::getTsCode).collect(Collectors.toSet());

            if (CollectionUtils.isEmpty(keys)) {
                return Boolean.FALSE;
            }

            List<SharesDaily> list = sharesDailyService.lambdaQuery()
                    .in(SharesDaily::getTsCode, keys)
                    .eq(SharesDaily::getTradeDate, LocalDateTimeUtil.toString(time))
                    .list();

            Map<String, SharesDaily> sharesDailyMap = Maps.uniqueIndex(list, SharesDaily::getTsCode);

            y.stream().forEach(item -> item.setId(sharesDailyMap.get(item.getTsCode()).getId()));

            log.info("涨跌停数据，list:{}", JSONObject.toJSONString(y));

            return sharesDailyService.updateBatchById(y);

        });

        log.debug("处理股票涨停时间已经结束, {}", JSONObject.toJSONString(localEvent));
    }

    @EventListener
    @Order(value = 1)
    @Async
    public void sharesDailyOnceStopEvent(LocalEvent localEvent) {
        log.debug("开始处理曾涨跌停数据， localEvent: {}", JSONObject.toJSONString(localEvent));
        LocalDateTime time = buildLocalDateTime(localEvent);

        if(Objects.isNull(time)) {
            return;
        }

        //  处理曾涨跌停数据
        sharesDailyService.onceStop(time, new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time), Constant.ONCE_UP_STOP)));

        sharesDailyService.onceStop(time, new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time), Constant.ONCE_DOWN_STOP)));

        log.debug("处理曾涨跌停数据已经结束, {}", JSONObject.toJSONString(localEvent));
    }

    private LocalDateTime buildLocalDateTime(LocalEvent localEvent) {
        //  统计的上下文
        SharesDailyService.SharesDailyReq sharesDailyReq = localEvent.getData(SharesDailyService.SharesDailyReq.class);
        //  处理时间（暂时根据时间处理）

        String tradeDate = sharesDailyReq.getTrade_date();

        if(StringUtils.isEmpty(tradeDate)) {
            return null;
        }
        return LocalDateTimeUtil.parseToLocalDateToLocalDateTime(tradeDate);
    }

    @EventListener
    @Order(value = 2)
    @Async
    public void sharesDailyGreaterOrDropEvent(LocalEvent localEvent) {
        log.debug("数据涨跌幅度， localEvent: {}", JSONObject.toJSONString(localEvent));
        LocalDateTime time = buildLocalDateTime(localEvent);

        if(Objects.isNull(time)) {
            return;
        }

        //  处理曾涨跌停数据
        sharesDailyService.handler(time, new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time),
                Constant.UP_THAN_FIVE)), y -> {
            Set<String> keys = y.stream().map(SharesDaily::getTsCode).collect(Collectors.toSet());
            if(CollectionUtils.isEmpty(keys)) {
                return Boolean.FALSE;
            }

            sharesDailyService.lambdaUpdate()
                    .in(SharesDaily::getTsCode, keys)
                    .set(SharesDaily::getGreaterThanFive, Boolean.TRUE)
                    .update();
            return Boolean.TRUE;
        });

        sharesDailyService.handler(time, new JSONObject().fluentPut(Constant.QUESTION, String.format("%s %s", LocalDateTimeUtil.toString(time),
                Constant.DOWN_THAN_FIVE)), y -> {
            Set<String> keys = y.stream().map(SharesDaily::getTsCode).collect(Collectors.toSet());
            if(CollectionUtils.isEmpty(keys)) {
                return Boolean.FALSE;
            }

            sharesDailyService.lambdaUpdate()
                    .in(SharesDaily::getTsCode, keys)
                    .set(SharesDaily::getDropThanFive, Boolean.TRUE)
                    .update();
            return Boolean.TRUE;
        });

    }
}
