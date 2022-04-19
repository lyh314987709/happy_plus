package com.happy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shares_daily_statistical", autoResultMap = true)
public class SharesDailyStatistical {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交易时间
     */
    @TableField("trade_date")
    private LocalDate tradeDate;

    /**
     * 涨停数量
     */
    private Long upStopNumber;

    /**
     * 上涨数量
     */
    private Long upNumber;

    /**
     * 跌停数量
     */
    private Long downStopNumber;

    /**
     * 下跌数量
     */
    private Long downNumber;

    /**
     * 曾涨停数量
     */
    private Long onceUpStopNumber;

    /**
     * 曾跌停数量
     */
    private Long onceDownStopNumber;

    /**
     * 涨停开版次数（平均）
     */
    private Long openUpStopNumber;

    /**
     * 跌停开版次数（平均）
     */
    private Long openDownStopNumber;

    /**
     * 涨幅大于5%
     */
    private Long upHalfNumber;

    /**
     * 下跌大于5%
     */
    private Long downHalfNumber;

    /**
     * 涨停平均收益比列（不包括开版）
     */
    private BigDecimal upStopIncomeRatio;

    /**
     * 涨停平均收益比列(包括开版）
     */
    private BigDecimal openStopIncomeRatio;

    @TableField(value = "ext", typeHandler = FastjsonTypeHandler.class)
    private Ext ext;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Ext{
        /**
         * 连板情况
         */
        private Set<ConnectingPlate> plate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConnectingPlate{

        /**
         * 连板天数
         */
        private Integer connectingDay;

        /**
         * 数量
         */
        private Integer number;
    }

}
