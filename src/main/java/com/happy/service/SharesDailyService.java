package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.SharesDaily;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public interface SharesDailyService extends IService<SharesDaily> {


    List<SharesDaily> synchronization(SharesDailyReq req);

    /**
     * 根据时间纬度处理数据是否停
     * @param time
     */
    void isStop(LocalDateTime time);

    void isStop();

    void onceStop(LocalDateTime time);

    void onceStop();

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
