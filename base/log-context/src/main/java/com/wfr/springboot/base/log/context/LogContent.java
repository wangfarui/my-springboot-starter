package com.wfr.springboot.base.log.context;

import com.wfr.springboot.base.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志内容
 *
 * @author wangfarui
 * @since 2022/9/22
 */
public class LogContent extends HashMap<String, Object> {

    /**
     * 可以忽略重写 toString 方法的类
     */
    private static final List<Class<?>> IGNORE_OVERRIDE_CLASS = new ArrayList<>(32);

    /**
     * 缓存 Class 类是否具备重写 toString 方法的能力
     */
    private static final Map<Class<?>, Boolean> OVERRIDE_ABILITY_CACHE = new ConcurrentHashMap<>(32);

    static {
        // 常用类型 且 重写了toString()方法的
        IGNORE_OVERRIDE_CLASS.add(CharSequence.class);
        IGNORE_OVERRIDE_CLASS.add(Enum.class);

        // 基础类型及其包装类型
        IGNORE_OVERRIDE_CLASS.add(Integer.class);
        IGNORE_OVERRIDE_CLASS.add(Long.class);
        IGNORE_OVERRIDE_CLASS.add(Float.class);
        IGNORE_OVERRIDE_CLASS.add(Double.class);
        IGNORE_OVERRIDE_CLASS.add(Boolean.class);
        IGNORE_OVERRIDE_CLASS.add(Character.class);
        IGNORE_OVERRIDE_CLASS.add(Byte.class);
        IGNORE_OVERRIDE_CLASS.add(Short.class);
        IGNORE_OVERRIDE_CLASS.add(int.class);
        IGNORE_OVERRIDE_CLASS.add(long.class);
        IGNORE_OVERRIDE_CLASS.add(float.class);
        IGNORE_OVERRIDE_CLASS.add(double.class);
        IGNORE_OVERRIDE_CLASS.add(boolean.class);
        IGNORE_OVERRIDE_CLASS.add(char.class);
        IGNORE_OVERRIDE_CLASS.add(byte.class);
        IGNORE_OVERRIDE_CLASS.add(short.class);
    }

    /**
     * 返回日志实体列表
     *
     * @return List<LogEntry>
     */
    public List<LogEntry> logEntryList() {
        List<LogEntry> list = new ArrayList<>(this.size());
        for (Map.Entry<String, Object> entry : this.entrySet()) {
            list.add(LogEntry.of(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    private static boolean canIgnoreOverride(Object o) {
        Class<?> clazz = o.getClass();
        Boolean can;
        if ((can = OVERRIDE_ABILITY_CACHE.get(clazz)) != null) {
            return can;
        } else {
            for (Class<?> c : IGNORE_OVERRIDE_CLASS) {
                if (c.isAssignableFrom(clazz)) {
                    can = Boolean.TRUE;
                    break;
                }
            }
        }
        if (can == null) {
            can = Boolean.FALSE;
        }
        OVERRIDE_ABILITY_CACHE.put(clazz, can);
        return can;
    }

    public static final class LogEntry {

        final String key;

        final Object value;

        private LogEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        static LogEntry of(String key, Object value) {
            return new LogEntry(key, value);
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public String getValueStr() {
            if (canIgnoreOverride(this.value)) {
                return this.value.toString();
            }
            return JsonMapper.toJson(this.value);
        }
    }

}
