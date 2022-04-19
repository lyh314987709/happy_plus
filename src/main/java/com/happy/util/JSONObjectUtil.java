package com.happy.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class JSONObjectUtil {

    /**
     * 将一个对象转换成JSONObject
     * @param req
     * @return
     */
    public static JSONObject toJSONObject(Object req) {
        return JSONObject.parseObject(JSONObject.toJSONString(req));
    }

    /**
     * 将字符串转换成具体的实体对象
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSONObject.parseObject(text, clazz);
    }

    /**
     * 将JSNObject转换成具体的实体
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(JSONObject json, Class<T> clazz) {
        return parseObject(JSONObject.toJSONString(json), clazz);
    }

    /**
     * 将JSONObject的集合转换成集合对象
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(List<JSONObject> list, Class<T> clazz) {
        return JSONObject.parseArray(JSONObject.toJSONString(list), clazz);
    }

    /**
     * 将集合对象转换成集合JSONObject
     * @param list
     * @return
     */
    public static List<JSONObject> toJSONObject(List list) {
        if(CollectionUtils.isEmpty(list)) {
            return ImmutableList.of();
        }
        return JSONObject.parseArray(JSONObject.toJSONString(list), JSONObject.class);
    }
}
