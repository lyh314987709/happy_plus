package com.happy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 股票与分类的关联关系（中间表）
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shares_intermediate", autoResultMap = true)
public class SharesIntermediate {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("shares_type_name")
    private String sharesTypeName;

    @TableField("ts_code")
    private String tsCode;
}
