package com.wfr.springboot.base.json.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON数据对象Mapper
 *
 * @author wangfarui
 * @since 2022/9/5
 */
public abstract class JsonMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    /**
     * 获取默认对象映射器
     *
     * @return 对象映射器
     */
    public static ObjectMapper defaultObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 将Object对象转换为json数据
     *
     * @param o 待转换的数据对象
     * @return json数据
     */
    public static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.warn("[JsonMapper][toJson]转换json数据异常: {}", e.toString());
        }
        return o.toString();
    }

    /**
     * 将json数据转换为Object对象
     *
     * @param json       json数据
     * @param objectType Object对象类型
     * @param <T>        Class对象类型
     * @return Object对象实例
     */
    public static <T> T toObject(String json, Class<T> objectType) {
        try {
            return OBJECT_MAPPER.readValue(json, objectType);
        } catch (JsonProcessingException e) {
            LOGGER.warn("[JsonMapper][toObject]转换json数据异常: {}", e.toString());
        }
        return null;
    }
}
