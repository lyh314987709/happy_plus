package com.happy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.happy.anno.FiledConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 股票每天的交易信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shares_daily", autoResultMap = true)
public class SharesDaily {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("ts_code")
    @FiledConvert("ts_code 股票代码")
    private String tsCode;

    /**
     * 名称
     */
    private String name;

    @TableField("trade_date")
    @FiledConvert("trade_date")
    private String tradeDate;

    /**
     * 开盘价
     */
    @TableField("open_amount")
    @FiledConvert("open")
    private BigDecimal open;

    /**
     * 最高加
     */
    @TableField("high")
    @FiledConvert("high")
    private BigDecimal high;

    /**
     * 最低价
     */
    @TableField("low")
    @FiledConvert("low")
    private BigDecimal low;

    /**
     * 收盘价
     */
    @TableField("close_amount")
    @FiledConvert("close")
    private BigDecimal close;

    /**
     * 昨日收盘价
     */
    @TableField("pre_close")
    @FiledConvert("pre_close")
    private BigDecimal perClose;

    /**
     * 涨跌额
     */
    @TableField("change_amount")
    @FiledConvert("change")
    private BigDecimal change;

    /**
     * 涨跌额
     */
    @TableField("pct_chg")
    @FiledConvert("pct_chg")
    private BigDecimal pctChg;

    /**
     * 成交量
     */
    @TableField("vol")
    @FiledConvert("vol")
    private BigDecimal vol;

    /**
     * 成交额
     */
    @TableField("amount")
    @FiledConvert("amount")
    private BigDecimal amount;

    @TableField("up_down")
    private UpDown upDown;

    @TableField("stop")
    @FiledConvert("涨跌停 曾涨停 曾跌停")
    private String stop;

    @TableField("stop_reason")
    @FiledConvert("涨停原因 跌停原因")
    private String stopReason;

    /**
     * 最终时间
     */
    @FiledConvert("最终跌停时间 最终涨停时间")
    private String endStopTime;

    /**
     * 开始时间
     */
    @FiledConvert("首次跌停时间 首次涨停时间")
    private String startStopTime;
    /**
     * 天数
     */
    @FiledConvert("连续涨停天数 连续跌停天数")
    private int days;

    @FiledConvert("开板次数")
    private int openFrequency;

    /**
     * 涨幅大于5%
     */
    private Boolean greaterThanFive;

    /**
     * 跌幅大于5%
     */
    private Boolean dropThanFive;

    @Getter
    @NoArgsConstructor
    public enum UpDown {
        UP,
        DEF,
        DOWN
    }

    public SharesDaily build(Shares shares) {

        this.name = Optional.ofNullable(shares).map(Shares::getName).orElse("EMPTY");

        this.upDown = UpDown.DEF;
        if(this.pctChg.compareTo(BigDecimal.ZERO) >0) {
            this.upDown = UpDown.UP;
        }

        if(BigDecimal.ZERO.compareTo(this.pctChg) > 0) {
            this.upDown = UpDown.DOWN;
        }
        return this;
    }

}
