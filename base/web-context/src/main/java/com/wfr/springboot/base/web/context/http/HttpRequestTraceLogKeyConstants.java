package com.wfr.springboot.base.web.context.http;

/**
 * http请求链路日志key 常量类
 *
 * @author wangfarui
 * @since 2022/7/21
 */
public abstract class HttpRequestTraceLogKeyConstants {

    public static final String REQUEST_METHOD = "http_request_method";

    public static final String REQUEST_URI = "http_request_uri";

    public static final String REQUEST_PARAM = "http_request_param";

    public static final String REQUEST_BODY = "http_request_body";

    public static final String REQUEST_RESPONSE_STATUS = "http_response_code";

    public static final String REQUEST_RESPONSE_BODY = "http_response_body";

}
