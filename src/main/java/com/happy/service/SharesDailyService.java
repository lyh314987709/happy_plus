package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.SharesDaily;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface SharesDailyService extends IService<SharesDaily> {


    List<SharesDaily> synchronization(SharesDailyReq req);

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
