package com.happy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.happy.net.ClientService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class LocalUtilTest {

    static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVU0VSX0NPREUiOiI0N2M3MDM4ZjM1M2M0OTZlYWM4NmRiYjc5ZmFkMWQxMiIsIkxPR0lOX1RJTUUiOiIxNjQ5OTkwNDU3MzYzIiwiVE9LRU5fVFlQRSI6InBjIn0.ulGeL4zYzdFMHvpSknLhUlaTminVJGsUxezcM1bm-Jk";



    public static void main(String[] args) {

        //doHandlerLog("/Users/apple/Documents/order_txt.txt", "http://mp.yunlizhi.cn/hope-saas-audit-web/otmsBill/initShppingOrderBill", token, false);
       // postOmsPlus(ImmutableSet.of("D2202010000840000698"), "http://otms.yunlizhi.cn/tms_plus_waybill/oms_plus/event/private/bill/create", token);


        //post("http://mp.yunlizhi.cn/hope-saas-audit-web/create_bill/init_order_bill", "[{\"carrierCode\":\"HZ1639140613829\",\"carrierTenantName\":\"临时客户-聂记食品\",\"checkGroupSign\":false,\"finishSum\":\"2270.0000kg, 0.0000方, 0.0000件\",\"goodsValue\":0.00000,\"loadSum\":\"2270.0000kg, 0.0000方, 0.0000件\",\"omsFeature\":39,\"orderCode\":\"D2203190300020000000\",\"orderCreateTime\":1647656815135,\"orderStatus\":6,\"receiptStation\":\"东升街道口岸路456号成都双流航空经济区\",\"signTime\":1647863750000,\"specificOrders\":[{\"auditSubjectNo\":\"服务费\",\"estimateOriginalValue\":2951,\"specificCode\":\"D2203190300020000000\",\"status\":6}],\"startStationName\":\"大沥镇彰德冷库(粤泰路)\",\"startTime\":1647703536000,\"tenantId\":\"T10000030002\",\"totalSum\":\"2270.0000kg, 0.0000方, 0.0000件\"}]", token);
        //doHandler("/Users/apple/Documents/text.txt", "http://mp.yunlizhi.cn/hope-saas-audit-web/otmsBill/initShppingOrderBill", token, false);

        //doHandler("/Users/apple/Documents/order_txt.txt", "http://mp.yunlizhi.cn/hope-saas-audit-web/otmsBill/initOrderBill", token, false);

        test(token);

        //testOrder(token);

        //billCheck();

        //billCheckPage();

        //String txt = "07a7e7f8f3c4ebce4edb3081d8e8d021,19785ed3f214645738825910ba0a65b9,1cb97dd7d7c0696935fa3334e93f4bda,1d17dc3f4792a96a312f66847f49fd7a,1d96282c6ed558e193209d5189781ca9,1e12978a735d3fe194cd57df5a1608ac,1f5ce8ebe8f691d3b0c912e2bceba4f8,23dd1ae00fbecae9bbe111450c4993a8,33b449db4704c34b5233279c4c372c81,35522d597bf1c552ae935db63c4294ec,3728585ef969f48b5a2a80462a82c68e,3aa9d96e9f63e679d5a086d6246829d6,548a1f5912d153b12365aa1b781b7ecd,5c2210d9caf60efec0c9aa1003c2b2c2,5c5606253a4eb20f1b81df700df80149,5c9b6a24cdb928c64264acb3a23ad5e0,5d55e189c5447f7aa03c244638a5fd41,5dd505914003903269b3dc37e73ca251,668036d7412a51309353f59a186bd549,6b1b869a146c47df473cae77fe83128c,6b7cd3e16cfdd1dadadc6e078bd8efa6,6c425db6b6cece9dfae7912c6913e32c,72dfa462e859579d76a36c2ca2ad1372,77c3ea07328941d52098cf6b0de2e1f1,78d03a4cd99c22c2ad8b5f702dbba6d0,7965555aaacb835ee537f2b07140b71f,7b33d5ccb16f6393f236aa130036c203,7cf5dd8d47c0dfceabd86352458dfd0d,7ead1510032f9b48a8649a808029007b,8127b8f8cf590a1cb5c76c7332acb1b6,873f4fad6e55acc47e1776806d428f7a,936d0538d8d90fa2e92abed98f755ae8,9427a6a35e3d6be20239a83a864d8401,9e6cfc7c0a89b0434b2327be7e39ba15,a26eb5d995b91906ae441c8352b438bd,a78c393490cc1964f90cb64a46b5b3f6,acf8e1a3a377038bc760ed386a5f87fa,b8cc33659528195c9f5af880d7316bf9,bea5ac19737656620046299e77a945b9,bffc6d6b27acb40fbba9f71aefb7e8ff,c11c87b277801f8b1509379f01e375a8,c1fb63181af1b1399a9fc5589bfd6505,ce4f0cb10cc61aa6b0b5834d50408bef,cfa302e088eae7d3ecb27f6df99de9b2,d5fd7c817c513f7f2d618965d95881d9,de46b448f00acf9a3551343303cd0398,e318e279f3aaee24b4b951bc0b2bbc14,ed5e259fb9c5363376d16dc398d29520,f8bfd208164257b4fd90ae3514a30dd0,fe1bc9b192fccca66f65975138d2136e";

        //ImmutableSet.copyOf(Splitter.on(",").trimResults().split(txt)).stream().forEach(item -> withdraw(item));
        //withdraw("b5fc2536506144661c5c5f4973c70c66");

        //System.out.println(1 << 2);

    }


    private static final String paymentCode = "P2022031617090002653";

    private static void billCheckPage() {
        String result = post("http://ylz.fresh-scm.cn/hope-saas-btm-web/btmBankcard/abnormalPayment/list", "{\"pageNum\":1,\"pageSize\":50,\"acctName\":\"邱华任\"}", token);

        JSONObject json = JSONObject.parseObject(result);

        JSONObject data = JSONObject.parseObject(json.getString("data"));

        JSONArray list = data.getJSONArray("list");

        Index build = Index.builder().index(1).build();

        Map<String, String> paymentCodeIdMap = ImmutableList.copyOf(list)
                .stream()
                .map(item -> JSONObject.parseObject(JSONObject.toJSONString(item))).
                collect(Collectors.toMap(item -> item.getString("sn"), item -> item.getString("paymentId")));


        paymentCodeIdMap.entrySet().stream().forEach(item -> {
            build.println();
            JSONObject jsonObject = billCheck(item.getKey());
            System.out.println(jsonObject);
            if("FAIL".equals(jsonObject.getString("status"))) {
                withdraw(item.getValue());
            }

            build.add();
        });


        System.out.println("重新提现完成");

    }

    private static void billCheck() {
        ImmutableList.copyOf(Splitter.on(",").trimResults()
                .split(paymentCode))
                .stream()
                .map(item -> new JSONObject().fluentPut("orderNo", item))
                .forEach(item -> {
                    String result = post("https://bms-plus.yunlizhi.cn/btm_plus/trade/withdraw/detail", JSONObject.toJSONString(item), token);

                    JSONObject json = JSONObject.parseObject(result);

                    JSONObject data = JSONObject.parseObject(json.getString("data"));

                    System.out.println(data);
                    System.out.println(String.format("%s-%s", item.getString("orderNo"), data.getString("status")));
                });
    }

    private static JSONObject billCheck(String sn) {
        System.out.println(sn);
        String result = post("https://bms-plus.yunlizhi.cn/btm_plus/trade/withdraw/detail",
                JSONObject.toJSONString(new JSONObject().fluentPut("orderNo", sn)),
                token);

        JSONObject json = JSONObject.parseObject(result);

        return JSONObject.parseObject(json.getString("data"));
    }

    private static void withdraw(String paymentId) {
        String result = post("http://ylz.fresh-scm.cn/hope-saas-btm-web/btmBankcard/abnormalPayment/withdraw",
                JSONObject.toJSONString(new JSONObject().fluentPut("paymentId", paymentId).fluentPut("tenantId", "T10").fluentPut("tenantName", "运荔枝管理租户")),
                token);

        System.out.println(result);
    }


    public static String  post(String url, String format, String token) {
        log.debug(format);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), format);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Token", token)
                .post(requestBody)
                .build();

        try {
            Response execute = ClientService.execute(request);

            String result = execute.body().string();
            //System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Strings.EMPTY;
    }

    public static void get(String url, String token){
        try {
            Response execute = ClientService.execute(new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .addHeader("Token", token)
                    .build());

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


    public static final String text1 = "F2204121440040000032";

    public static void test(String token) {
        Set<String> shippingOrderCodes = ImmutableSet.copyOf(Splitter.on(",").trimResults().split(text1));

        String url = "https://bms-plus.yunlizhi.cn/bms_plus/decoupling/private/repair_bill/shipping";

        Index build = Index.builder().index(0).build();

        shippingOrderCodes.stream().forEach(item -> {
            String formatUrl = String.format("%s?refCode=%s", url, item);
            get(formatUrl, token);
            build.println();
            build.add();
        });

        System.out.println("执行完成");
    }

    public static final String ORDER_TEXT = "D2204010000840012472";

    public static void testOrder(String token) {
        Set<String> shippingOrderCodes = ImmutableSet.copyOf(Splitter.on(",").trimResults().split(ORDER_TEXT));

        String url = "https://bms-plus.yunlizhi.cn/bms_plus/decoupling/private/repair_bill/single/order";

        Index build = Index.builder().index(0).build();

        shippingOrderCodes.stream().forEach(item -> {
            String formatUrl = String.format("%s?orderCode=%s", url, item);
            get(formatUrl, token);
            build.println();
            build.add();
        });

        System.out.println("执行完成");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Index {
        private Integer index;

        public void add() {
            this.index = this.index+1;
        }

        public void println() {
            System.out.println(this.index);
        }
    }


}
