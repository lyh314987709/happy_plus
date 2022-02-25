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
import java.time.LocalDate;

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
    @FiledConvert("ts_code")
    private String tsCode;

    @TableField("trade_date")
    @FiledConvert("trade_date")
    private LocalDate tradeDate;

    @TableField("open_amount")
    @FiledConvert("open")
    private BigDecimal open;

    @TableField("high")
    @FiledConvert("high")
    private BigDecimal high;

    @TableField("low")
    @FiledConvert("low")
    private BigDecimal low;

    @TableField("close_amount")
    @FiledConvert("close")
    private BigDecimal close;

    @TableField("pre_close")
    @FiledConvert("pre_close")
    private BigDecimal perClose;

    @TableField("change_amount")
    @FiledConvert("change")
    private BigDecimal change;

    @TableField("pct_chg")
    @FiledConvert("pct_chg")
    private BigDecimal pctChg;

    @TableField("vol")
    @FiledConvert("vol")
    private BigDecimal vol;

    @TableField("amount")
    @FiledConvert("amount")
    private BigDecimal amount;

    @TableField("up_down")
    private UpDown upDown;

    @TableField("stop")
    private Stop stop;


    @Getter
    @NoArgsConstructor
    public enum UpDown {
        UP,
        DEF,
        DOWN
    }


    @Getter
    @NoArgsConstructor
    public enum Stop {
        UP,
        DEF,
        DOWN
    }

    public SharesDaily build() {
        this.upDown = UpDown.DEF;

        if(this.pctChg.compareTo(BigDecimal.ZERO) >0) {
            this.upDown = UpDown.UP;
        }

        if(BigDecimal.ZERO.compareTo(this.pctChg) > 0) {
            this.upDown = UpDown.DOWN;
        }

        this.stop = Stop.DEF;

        if(this.pctChg.compareTo(new BigDecimal("9.95")) >0) {
            this.stop = Stop.UP;
        }
        if(this.pctChg.compareTo(new BigDecimal("-9.95")) == -1) {
            this.stop = Stop.DOWN;
        }
        return this;
    }

}
