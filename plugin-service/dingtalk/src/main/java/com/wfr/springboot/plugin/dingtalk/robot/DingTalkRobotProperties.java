package com.wfr.springboot.plugin.dingtalk.robot;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.wfr.base.framework.common.utils.ExceptionUtils;
import com.wfr.base.framework.common.utils.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 钉钉机器人属性配置信息
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DingTalkRobotProperties implements InitializingBean {

    @Value("${dingTalk.client-url:https://oapi.dingtalk.com/robot/send}")
    private String clientUrl;

    @Value("${dingTalk.access-token:}")
    private String accessToken;

    @Value("${dingTalk.secret:}")
    private String secret;

    @Getter
    @Value("${dingTalk.enabled:false}")
    private boolean enabled;

    /**
     * 有secret加签时的最新时间戳（保持在半小时以内）
     */
    private long newestTimestamp;

    private final Object LOCK = new Object();

    /**
     * 全量请求url地址
     */
    private static String REQUEST_URL_CACHE;

    /**
     * 请求url地址的缓存时长（毫秒）
     */
    private static final long REQUEST_URL_CACHE_DURATION = 30 * 60 * 1000;

    private static final String[] REQUEST_URL_TEMPLATE = {"%s?access_token=%s", "%s?access_token=%s&timestamp=%s&sign=%s"};

    private static final String[] MONITOR_PROPERTIES_PARAMS = {"dingTalk.client-url", "dingTalk.access-token", "dingTalk.secret"};

    /**
     * 获取钉钉客户端请求路径url
     *
     * @return 全量请求路径url
     */
    public String getRequestUrl() {
        // 校验钉钉配置
        valid();

        if (StringUtils.isNotBlank(this.secret)) {
            synchronized (LOCK) {
                long nowTimestamp = System.currentTimeMillis();
                // 缓存为空 或 缓存超时
                if (REQUEST_URL_CACHE == null || (nowTimestamp - this.newestTimestamp > REQUEST_URL_CACHE_DURATION)) {
                    String sign = getCryptoSign(nowTimestamp);
                    REQUEST_URL_CACHE = generateRequestUrl(nowTimestamp, sign);
                    this.newestTimestamp = nowTimestamp;
                }
            }
        } else if (REQUEST_URL_CACHE == null) {
            REQUEST_URL_CACHE = generateRequestUrl(0L, null);
        }

        return REQUEST_URL_CACHE;
    }

    /**
     * 变更钉钉属性
     *
     * @param changeEvent 配置变更事件
     */
    public static void changeProperties(ConfigChangeEvent changeEvent) {
        for (String param : MONITOR_PROPERTIES_PARAMS) {
            if (changeEvent.isChanged(param)) {
                REQUEST_URL_CACHE = null;
                break;
            }
        }
    }

    private void valid() {
        if (StringUtils.isBlank(this.clientUrl)) {
            throw new IllegalArgumentException("钉钉url地址不能为空!");
        }
        if (StringUtils.isBlank(this.accessToken)) {
            throw new IllegalArgumentException("钉钉秘钥不能为空!");
        }
    }

    private String getCryptoSign(long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + this.secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(this.secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.warn("[DingTalkClient][getCryptoSign]钉钉客户端获取加密签名异常。" + ExceptionUtils.exceptionStackTraceText(e));
        }
        log.warn("[DingTalkClient][getCryptoSign]钉钉客户端获取加密签名未知异常，请检查秘钥配置信息！");
        return null;
    }

    private String generateRequestUrl(long timestamp, String sign) {
        if (timestamp != 0L && StringUtils.isNotBlank(sign)) {
            return String.format(REQUEST_URL_TEMPLATE[1], this.clientUrl, this.accessToken, timestamp, sign);
        } else {
            return String.format(REQUEST_URL_TEMPLATE[0], this.clientUrl, this.accessToken);
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            valid();
        } catch (IllegalArgumentException e) {
            this.enabled = false;
        }
    }
}
