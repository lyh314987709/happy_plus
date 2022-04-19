package com.happy.event.impl;

import com.happy.domain.SharesDaily;
import com.happy.domain.SharesDailyStatistical;
import com.happy.event.domian.LocalEvent;
import com.happy.service.SharesDailyService;
import com.happy.util.Constant;
import com.happy.util.LocalDateTimeUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.happy.util.Constant.UP_STOP;

@Component
@Slf4j
public class SharesDailyStatisticalEventService {

    @Autowired
    private SharesDailyService sharesDailyService;

    @EventListener
    @Order(value = 3)
    @Async
    public void statisticEvent(LocalEvent localEvent) {
        SharesDailyService.SharesDailyReq sharesDailyReq = localEvent.getData(SharesDailyService.SharesDailyReq.class);

        //  数据库原始数据
        List<SharesDaily> list = sharesDailyService.lambdaQuery()
                .eq(SharesDaily::getTradeDate, sharesDailyReq.getTrade_date())
                .list();

        StatisticalContent content = StatisticalContent.builder()
                .list(list)
                .statistical(SharesDailyStatistical.builder()
                        .tradeDate(LocalDateTimeUtil.parseToLocalDate(sharesDailyReq.getTrade_date()))
                        .build())
                .build();



    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StatisticalContent {

        private SharesDailyStatistical statistical;

        private List<SharesDaily> list;

        public Long count(Predicate<SharesDaily> predicate) {
            return this.list.stream().filter(predicate).count();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Strategy {
        /**
         * 计算涨停数量
         */
        UP_STOP_NUMBER(i -> i.getStatistical().setUpStopNumber(i.count(j -> UP_STOP.equals(j.getStop())))),

        /**
         * 计算跌停数量
         */
        DOWN_STOP_NUMBER(i -> i.getStatistical().setDownStopNumber(i.count(j -> Constant.DOWN_STOP.equals(j.getStop())))),

        /**
         * 统计上涨数量
         */
        UP_NUMBER(i -> i.getStatistical().setUpNumber(i.count(j -> SharesDaily.UpDown.UP.equals(j.getUpDown())))),

        /**
         * 统计下跌的数量
         */
        DOWN_NUMBER(i -> i.getStatistical().setDownNumber(i.count(j -> SharesDaily.UpDown.DOWN.equals(j.getStop())))),

        /**
         * 曾涨停
         */
        ONCE_UP_STOP(i -> i.getStatistical().setOnceUpStopNumber(i.count(l -> Constant.ONCE_UP_STOP.equals(l.getStop())))),

        /**
         * 曾跌停
         */
        ONCE_DOWN_STOP(i -> i.getStatistical().setOnceDownStopNumber(i.count(l -> Constant.ONCE_DOWN_STOP.equals(l.getStop()))));

        /**
         * 涨停平均开版次数
         */



        /**
         * 跌停平均开版次数
         */
        private Consumer<StatisticalContent> function;
    }
}
