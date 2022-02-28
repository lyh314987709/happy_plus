package com.happy.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface StatisticService {


    List<JSONObject> dailyList(DailyStopReq req);

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class DailyStopReq {

        /**
         * 交易时间
         */
        private LocalDate tradeDate;

        /**
         * 交易时间区间
         */
        private List<LocalDate> tradeDates;

        /**
         * 类型
         */
        private Set<String> sharesTypeNames;

        private String sharesTypeName;

        /**
         * 交易代码
         */
        private Set<String> tsCode;

        /**
         * 名称（股票）
         */
        private Set<String> names;

        /**
         * 名称（股票）
         */
        private String name;

        /**
         * 涨跌停 （涨停，跌停，曾涨停）
         */
        private String stop;

        /**
         * 涨幅区间
         */
        private List<BigDecimal> pctChgs;

        /**
         * 是否按类型分组
         */
        private Boolean groupByType;

        public void initQuery () {
            if(CollectionUtils.isEmpty(this.tradeDates) && Objects.isNull(this.tradeDate)) {
                this.tradeDate = LocalDate.now();
            }
        }

        public Boolean buildTsCode(Set<String> tsCodes) {
            if(CollectionUtils.isEmpty(this.tsCode)) {
                this.tsCode = tsCodes;
            }

            Sets.SetView<String> intersection = Sets.intersection(this.tsCode, tsCodes);

            if(CollectionUtils.isEmpty(intersection)) {
                return Boolean.FALSE;
            }

            this.tsCode = intersection;
            return Boolean.TRUE;
        }
    }

}
