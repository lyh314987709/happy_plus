package com.happy.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.SharesDaily;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface SharesDailyService extends IService<SharesDaily> {

    /**
     * 同步股票的交易数据
     * @param req
     * @return
     */
    List<SharesDaily> synchronization(SharesDailyReq req);



    void onceStop(LocalDateTime time, JSONObject json);

    void handler(LocalDateTime time, JSONObject json, Predicate<List<SharesDaily>> predicate);

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class SharesDailyReq {

        /**
         * 代码
         */
        private String ts_code;

        /**
         * 交易时间
         */
        private String trade_date;

        /**
         * 开始时间
         */
        private String start_date;

        /**
         * 结束时间
         */
        private String end_date;
    }
}
