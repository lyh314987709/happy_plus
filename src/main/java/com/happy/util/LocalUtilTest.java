package com.happy.util;

import com.google.common.collect.ImmutableList;
import com.happy.net.ClientService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class LocalUtilTest {

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVU0VSX0NPREUiOiI0N2M3MDM4ZjM1M2M0OTZlYWM4NmRiYjc5ZmFkMWQxMiIsIkxPR0lOX1RJTUUiOiIxNjQ1NzU2NDY2ODgwIiwiVE9LRU5fVFlQRSI6InBjIn0.g9fV1bFsO1meILV8-qlcC8br0cSDMhEBr2MTmem6iW8";
        doHandler("/Users/apple/Documents/text.txt", "http://mp.yunlizhi.cn/hope-saas-audit-web/otmsBill/initShppingOrderBill", token, false);
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

    public static void doHandler(String path, String url, String token, Boolean haveTry) {
        List<String> list = parseText(path);
        if(haveTry) {
            list = ImmutableList.of(list.get(0));
        }
        list.forEach(item -> post(url, item, token));
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
