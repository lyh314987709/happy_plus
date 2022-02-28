package com.happy.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.happy.net.ClientService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.execute.Execute;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class LocalUtilTest {

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVU0VSX0NPREUiOiI0N2M3MDM4ZjM1M2M0OTZlYWM4NmRiYjc5ZmFkMWQxMiIsIkxPR0lOX1RJTUUiOiIxNjQ2MDI4MzI2NzM3IiwiVE9LRU5fVFlQRSI6InBjIn0.ttb7h4cxa5ygc6-V_Op8HP8lXyTsLolvULVnNQ0gniY";
        //doHandlerLog("/Users/apple/Documents/order_txt.txt", "http://mp.yunlizhi.cn/hope-saas-audit-web/otmsBill/initShppingOrderBill", token, false);
        postOmsPlus(ImmutableSet.of("D2202230030040000128"), "http://otms.yunlizhi.cn/tms_plus_waybill/oms_plus/event/private/bill/create", token);
    }


    public static void  post(String url, String format, String token) {
        log.debug(format);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), format);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Token", token)
                .post(requestBody)
                .build();

        try {
            Response execute = ClientService.execute(request);
            System.out.println(execute.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void postOmsPlus(Set<String> orderCode, String url, String token) {
        JSONObject json = new JSONObject().fluentPut("odrNos", orderCode);
        post(url, JSONObject.toJSONString(json), token);
    }

    public static void doHandler(String path, String url, String token, Boolean haveTry) {
        List<String> list = parseText(path);
        if(haveTry) {
            list = ImmutableList.of(list.get(0));
        }
        list.forEach(item -> post(url, item, token));
    }

    public static void doHandlerLog(String path, String url, String token, Boolean haveTry) {
        List<String> list = parseText(path);
        list.stream().forEach(item -> {
            try {
                JSONObject.parseArray(item, JSONObject.class);
            } catch (Exception e) {
                log.debug(item);
            }
        });
    }


    public static List<String> parseText(String path) {
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String s = null;
            List<String> results = new ArrayList<>();
            while ( (s = reader.readLine()) != null) {
                if(s.length() > 30) {
                    String[] split = s.split("params = ");
                    String[] split1 = split[1].split(", result =");
                    String result = split1[0];
                    result = result.substring(1, result.length());
                    result = result.substring(0, result.length() - 1);
                    results.add(result);
                }
            }
            reader.close();
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return ImmutableList.of();
        }
    }
}
