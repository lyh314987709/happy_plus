package com.happy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.happy.anno.FiledConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 基本信息  https://tushare.pro/document/2?doc_id=25
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shares", autoResultMap = true)
public class Shares {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * TS代码
     */
    @TableField("ts_code")
    @FiledConvert("ts_code")
    private String tsCode;

    /**
     * 代码
     */
    @TableField("symbol")
    @FiledConvert("symbol")
    private String symbol;

    /**
     * 名称
     */
    @TableField("name")
    @FiledConvert("name")
    private String name;

    /**
     * 地狱
     */
    @TableField("area")
    @FiledConvert("area")
    private String area;

    /**
     * 所属行业
     */
    @TableField("industry")
    @FiledConvert("industry")
    private String industry;

    /**
     * 全称
     */
    @TableField("full_name")
    @FiledConvert("full_name")
    private String fullName;

    /**
     * 类型
     */
    @TableField("market")
    @FiledConvert("market")
    private String market;

    /**
     * 交易代码
     */
    @TableField("exchange")
    @FiledConvert("exchange")
    private String exchange;

    /**
     * 类型
     */
    @TableField("is_hs")
    @FiledConvert("is_hs")
    private String isHs;
}
