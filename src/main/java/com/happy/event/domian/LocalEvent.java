package com.happy.event.domian;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationEvent;

/**
 * 事件源
 */

public class LocalEvent extends ApplicationEvent {

    public static String SOURCE = "SHARES_DAILY";

    /**
     * 时间jianting
     */
    private Object data;

    private Object source;


    public LocalEvent(Object source, Object data) {
        super(source);
        this.data = data;
        this.source = source;
    }

    public LocalEvent(Object data) {
       this(SOURCE, data);
    }

    public Object getDate() {
        return data;
    }

    public Object getSource() {
        return this.source;
    }

    public <T> T getData(Class<T> clazz) {
        return JSONObject.parseObject(JSONObject.toJSONString(this.data), clazz);
    }
}
