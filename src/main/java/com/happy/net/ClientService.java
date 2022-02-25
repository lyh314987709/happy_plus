package com.happy.net;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.happy.util.Constant;
import lombok.*;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ClientService {

    List<Map<String, String>> post(Api api);

    List<Map<String, String>> post(Api api, JSONObject json);

    List<Map<String, String>> askMoneyGet(Api api, JSONObject json);

    List<Map<String, String>> askMoneyGet(Api api);

    static Response execute(Request request) throws Exception{
        return new OkHttpClient().newCall(request).execute();
    }


    default JSONObject getDefAskMoney() {
        return new JSONObject().fluentPut(Constant.QUESTION, Constant.DEF_KEY)
                .fluentPut(Constant.PER_PAGE, 5000)
                .fluentPut(Constant.QUERY_TYPE, Constant.STOCK);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class Query {

        private static String TOKEN = "cee7373f5534cd6ac10783e468db6710767cf637007930de27ce3a08";

        private static String URL = "http://api.tushare.pro";

        public static final MediaType JSON = MediaType.parse("application/json");

        private Api api;

        private JSONObject params;

        public Request post() {
            checkArgument();
            JSONObject apply = api.apply(params);

            String fields = apply.getString("fields");

            JSONObject queryParams = new JSONObject().fluentPut("api_name", api.getPath())
                    .fluentPut("token", TOKEN)
                    .fluentPut("params", apply);

            if(!StringUtils.isEmpty(fields)) {
                queryParams.fluentPut("fields", fields);
                apply.remove("fields");
            }

            RequestBody requestBody = RequestBody.create(JSON, JSONObject.toJSONString(queryParams));
            return new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
        }

        public Request askMoneyGet() {
            checkArgument();
            JSONObject apply = api.apply(params);
            Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(apply), Map.class);
            //  路径信息
            String url = api.getPath();
            if(!map.isEmpty()) {
                List<String> params = map.entrySet().stream().map(item -> String.format("%s=%s", item.getKey(), item.getValue())).collect(Collectors.toList());
                String join = Joiner.on("&").skipNulls().join(params);
                url = String.format("%s?%s", url, join);
            }
            System.out.println("url :" + url);
            return new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .build();

        }

        public void checkArgument() {
            Preconditions.checkArgument(Objects.nonNull(api), "API对象不能为空");
        }


    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class ResponseBody {
        private String request_id;

        private Integer code;

        private String msg;

        private ResponseData data;

        public List<Map<String, String>> result() {
            List<String> fields = this.data.getFields();
            List<List<String>> items = this.data.getItems();
            return items.stream().map(item -> this.buildMap(item, fields)).collect(Collectors.toList());
        }

        public Map<String, String> buildMap(List<String> item, List<String> field) {

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < field.size(); i++) {
                map.put(field.get(i), item.get(i));
            }
            return map;
        }
    }

    /**
     * iwencai接口的返回值
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class AskMoneyResp {
        private String status_code;

        private String status_msg;

        private AskMoneyDataResp data;

        public List<Map<String, String>> formData() {
            return Optional.ofNullable(this.data)
                    .orElse(AskMoneyDataResp.builder().data(ImmutableList.of()).build())
                    .formData();
        }

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class AskMoneyDataResp {
        private List<Map<String, String>> data;

        public List<Map<String, String>> formData() {
            return this.data.stream().map(item -> {
                Map<String, String> formMap = new HashMap<>();
                item.entrySet().stream().forEach(i -> formMap.put(changeCharset(i.getKey()), changeCharset(i.getValue())));
                return formMap;
            }).collect(Collectors.toList());
        }
    }


    static String changeCharset(String key) {
        if(StringUtils.isEmpty(key)) {
            return key;
        }
        return changeCharset(key.getBytes());
    }


    static String changeCharset(byte[] str) {
        return changeCharset(str, "utf-8");
    }


    static String changeCharset(byte[] str, String newCharset){
        try {
            if (Objects.nonNull(str)) {
                return new String(str, newCharset);
            }
            return Strings.EMPTY;
        } catch (Exception e) {
            return Strings.EMPTY;
        }

    }

    @AllArgsConstructor
    @Getter
    enum Api {

        STOCK_BASIC("stock_basic", "股票列表", t -> Optional.ofNullable(t).orElse(new JSONObject()).fluentPut("list_status", "L")),
        DAILY("daily", "日线行情", t -> Optional.ofNullable(t).orElse(new JSONObject())),
        LIMIT_LIST("limit_list", "每日涨跌停统计", t -> Optional.ofNullable(t).orElse(new JSONObject())),
        TOP10("hsgt_top10", "沪深股通十大成交股", t -> Optional.ofNullable(t).orElse(new JSONObject())),
        ASK_MONEY_STRATEGY("http://www.iwencai.com/unifiedwap/unified-wap/result/get-stock-pick", "iwencai策略查询接口", t -> Optional.ofNullable(t).orElse(new JSONObject()));

        private String path;
        private String message;
        private Function<JSONObject, JSONObject> function;

        public JSONObject apply(JSONObject json) {
           return this.function.apply(json);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class ResponseData {
        private List<String> fields;

        private List<List<String>> items;
    }
}
