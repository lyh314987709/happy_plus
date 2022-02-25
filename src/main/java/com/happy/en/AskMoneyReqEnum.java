package com.happy.en;

import com.alibaba.fastjson.JSONObject;
import com.happy.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  AskMoneyReqEnum {

    /**
     * 问财股票分类查询接口
     */
    SHARES_TYPE(() -> new JSONObject().fluentPut(Constant.QUESTION, Constant.DEF_KEY)
            .fluentPut(Constant.PER_PAGE, 5000)
            .fluentPut(Constant.QUERY_TYPE, Constant.STOCK));

    private Supplier<JSONObject> supplier;

    public JSONObject get() {
        return supplier.get();
    }



}
