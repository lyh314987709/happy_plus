package com.happy.net;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService{
    public static void main(String[] args) throws UnsupportedEncodingException {
       System.out.println(JSONObject.toJSONString(new ClientServiceImpl().post(Api.DAILY, new JSONObject()
               .fluentPut("trade_date", "20220223").fluentPut("ts_code", "000001.SZ"))));
        //System.out.println(JSONObject.toJSONString(new ClientServiceImpl().post(Api.LIMIT_LIST, new JSONObject().fluentPut("limit_type", "U"))));
        /*System.out.println(JSONObject.toJSONString(new ClientServiceImpl().post(Api.TOP10, new JSONObject().fluentPut("trade_date", "20220221")
              .fluentPut("market_type", "1"))));*/
        //System.out.println(JSONObject.toJSONString(new ClientServiceImpl().askMoneyGet(Api.ASK_MONEY_STRATEGY, new JSONObject().fluentPut("question", "所属概念"))));


       /* List<Map<String, String>> maps = new ClientServiceImpl().askMoneyGet(Api.ASK_MONEY_STRATEGY, new JSONObject().fluentPut("question", "所属概念")
                .fluentPut("perpage", 5000)
                .fluentPut("query_type", "stock"));
        System.out.println(maps.size());*/

    }

    @Override
    public List<Map<String, String>> post(Api api){
        return post(api, null);
    }

    @Override
    public List<Map<String, String>> post(Api api, JSONObject json) {
        Request request = Query.builder().api(api).params(json).build().post();
        try {
            Response execute = ClientService.execute(request);
            if( execute.isSuccessful()) {
                String body = execute.body().string();
                log.debug("路径：{}, 接口原始返回参数：{}", api.getPath(), JSONObject.toJSONString(body));
                ResponseBody responseBody = JSONObject.parseObject(body, ResponseBody.class);
                return responseBody.result();
            }
            return ImmutableList.of();
        }catch (Exception e) {
            log.error("调用接口失败！， 接口路径：{}", api.getPath());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Map<String, String>> askMoneyGet(Api api, JSONObject json) {
        Request request = Query.builder().api(api).params(json).build().askMoneyGet();
        try {
            Response execute = ClientService.execute(request);
            if(execute.isSuccessful()) {
                AskMoneyResp responseBody = JSONObject.parseObject(execute.body().string(), AskMoneyResp.class);
                return responseBody.formData();
            }
            return ImmutableList.of();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用接口失败！， 接口路径：{}", api.getPath());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Map<String, String>> askMoneyGet(Api api) {
        return askMoneyGet(api, null);
    }

}
