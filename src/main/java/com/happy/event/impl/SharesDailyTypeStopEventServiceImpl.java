package com.happy.event.impl;

import com.alibaba.fastjson.JSONObject;
import com.happy.event.domian.LocalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SharesDailyTypeStopEventServiceImpl {


    @EventListener
    @Order(value = 1)
    @Async
    public void sharesDailyTypeStopEvent(LocalEvent localEvent) {
        System.out.println(JSONObject.toJSONString(localEvent));
    }
}
