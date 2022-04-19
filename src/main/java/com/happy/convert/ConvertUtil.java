package com.happy.convert;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.happy.anno.FiledConvert;
import com.happy.util.Kit;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertUtil {


    public static Map<String, Map<String, String>> CACHE = new HashMap<>();

    public static Map<String, String> filedConvertAnalysis(Class<?> clazz) {

        String className = clazz.getName();
        Map<String, String> cacheMap = CACHE.get(className);
        if(Objects.nonNull(cacheMap)) {
            return cacheMap;
        }
        Map<String, String> map = new HashMap<>();
        //  获取属性
        ImmutableList.copyOf(clazz.getDeclaredFields())
                .stream()
                .filter(item -> Objects.nonNull(item.getAnnotationsByType(FiledConvert.class)) && item.getAnnotationsByType(FiledConvert.class).length > 0)
                .forEach(item -> map.put(item.getAnnotationsByType(FiledConvert.class)[0].value(), item.getName()));

        CACHE.put(className, map);
        return map;
    }

    public static <T> List<T> convert(List<Map<String, String>> list, Class<T> clazz) {
        //  字段映射信息
        Map<String, String> filedMap = ConvertUtil.filedConvertAnalysis(clazz);

        List<Map<String, String>> result = list.stream().map(item -> {
            Map<String, String> new_map = new HashMap<>();
            item.entrySet().stream().forEach(i -> {
                new_map.put(filedMap.get(i.getKey()), i.getValue());
            });
            return new_map;
        }).collect(Collectors.toList());

        return JSONObject.parseArray(JSONObject.toJSONString(result), clazz);
    }

    public static <T> List<T> convertLike(List<Map<String, String>> list, Class<T> clazz) {
        //  字段映射信息
        Map<String, String> filedMap = ConvertUtil.filedConvertAnalysis(clazz);

        Set<String> keySet = filedMap.keySet();

        List<Map<String, String>> result = list.stream().map(item -> {
            Map<String, String> new_map = new HashMap<>();
            item.entrySet().stream().forEach(i -> {
                if(Objects.isNull(i.getValue())) {
                    return;
                }
                new_map.put(filedMap.get(Kit.contain(keySet, i.getKey())), i.getValue());
            });
            return new_map;
        }).collect(Collectors.toList());

        return JSONObject.parseArray(JSONObject.toJSONString(result), clazz);
    }
}
