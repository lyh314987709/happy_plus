package com.happy.en;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.happy.convert.ConvertUtil;
import com.happy.domain.SharesDaily;
import com.happy.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  AskMoneyReqEnum {

    /**
     * 问财股票分类查询接口
     */
    SHARES_TYPE(t -> t.fluentPut(Constant.QUESTION, Constant.DEF_KEY)
            .fluentPut(Constant.PER_PAGE, 5000)
            .fluentPut(Constant.QUERY_TYPE, Constant.STOCK), y -> sharesTypesResult(y)),

    /**
     * 涨跌停查询接口
     */
    STOP(t -> t.fluentPut(Constant.PER_PAGE, 5000)
            .fluentPut(Constant.QUERY_TYPE, Constant.STOCK), y -> JSONObject.parseArray(JSONObject.toJSONString(ConvertUtil.convertLike(y, SharesDaily.class)), JSONObject.class));

    private Function<JSONObject, JSONObject> function;

    private Function<List<Map<String, String>>, List<JSONObject>> resultFunction;

    public JSONObject apply(JSONObject json) {
        return this.function.apply(json);
    }

    public JSONObject apply() {
         return apply(new JSONObject());
    }

    public List<JSONObject> result(List<Map<String, String>> list) {
        return this.resultFunction.apply(list);
    }

    /**
     * 处理类型
     * @param list
     * @return
     */
    public static List<JSONObject> sharesTypesResult(List<Map<String, String>> list) {
        List<String> types = list.stream()
                .map(item -> item.get(Constant.DEF_KEY))
                .collect(Collectors.toList());

        String join = Joiner.on("；").skipNulls().join(types);
        Set<String> typeResult = ImmutableSet.copyOf(Splitter.on("；").trimResults().omitEmptyStrings().split(join));
        return typeResult.stream().map(item -> new JSONObject().fluentPut("typeName", item)).collect(Collectors.toList());
    }
}
