package com.happy.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

public class Kit {

    public static <T> List<T> parseArray(List<JSONObject> list, Class<T> clazz) {
        return JSONObject.parseArray(JSONObject.toJSONString(list), clazz);
    }

    public static String contain(Set<String> set, String key) {
        return set.stream().filter(item -> contain(item, key)).findFirst().orElse(key);
    }

    public static Boolean contain(String message, String key) {
        return ImmutableSet.copyOf(message.split(" ")).stream().anyMatch(item -> key.contains(item));
    }

    public static List<JSONObject> toJSONObject(List list) {
        if(CollectionUtils.isEmpty(list)) {
            return ImmutableList.of();
        }
        return JSONObject.parseArray(JSONObject.toJSONString(list), JSONObject.class);
    }
}
