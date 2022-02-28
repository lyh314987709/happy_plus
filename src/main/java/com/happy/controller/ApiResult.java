package com.happy.controller;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResult<T> {


    private static final int OK_HTTP_CODE = 200;

    private static final int ERROR_HTTP_CODE = -200;

    private static final String DEF_SUCCESS = "成功";

    private static final String DEF_ERROR = "失败";

    protected T data;

    protected int code;

    @JSONField(alternateNames = {"message"})
    protected String msg;


    private static ApiResultBuilder successBuilder() {
        return ApiResult.builder().code(OK_HTTP_CODE).msg(DEF_SUCCESS);
    }

    public static ApiResult success() {
        return successBuilder().build();
    }

    public static <T> ApiResult<T> success(T date) {
        return successBuilder().data(date).build();
    }

    public static ApiResultBuilder errorBuilder(String msg) {
        return ApiResult.builder().code(ERROR_HTTP_CODE).msg(msg);
    }

    public static ApiResult error() {
        return errorBuilder(DEF_ERROR).build();
    }

    public static ApiResult error(String msg) {
        return errorBuilder(msg).build();
    }


}
