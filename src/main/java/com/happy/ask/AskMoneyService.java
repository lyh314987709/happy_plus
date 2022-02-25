package com.happy.ask;

import com.alibaba.fastjson.JSONObject;
import com.happy.en.AskMoneyReqEnum;
import com.happy.net.ClientService;

import java.util.List;
import java.util.Map;

public interface AskMoneyService {

    List<Map<String, String>> defGet(ClientService.Api api, AskMoneyReqEnum askReq);

    List<Map<String, String>> defGet(ClientService.Api api, JSONObject json);

}
