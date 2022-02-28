package com.happy.ask.impl;

import com.alibaba.fastjson.JSONObject;
import com.happy.ask.AskMoneyService;
import com.happy.en.AskMoneyReqEnum;
import com.happy.net.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AskMoneyServiceImpl implements AskMoneyService {

    @Autowired
    private ClientService clientService;


    @Override
    public List<Map<String, String>> defGet(ClientService.Api api, AskMoneyReqEnum askReq) {
        return clientService.askMoneyGet(ClientService.Api.ASK_MONEY_STRATEGY, askReq.apply());
    }

    @Override
    public List<Map<String, String>> defGet(ClientService.Api api, AskMoneyReqEnum askReq, JSONObject json) {
        return clientService.askMoneyGet(ClientService.Api.ASK_MONEY_STRATEGY, askReq.apply(json));
    }
}
