package com.wfr.springboot.base.dao.context;

/**
 * sql方法类型
 *
 * @author wangfarui
 * @since 2022/10/8
 */
public enum SqlMethod {
    SELECT,
    UPDATE,
    INSERT,
    DELETE,
    UNKNOWN;

    public static SqlMethod getSqlMethod(String sql) {
        if (sql.length() < 6) {
            return UNKNOWN;
        }
        String sqlPrefix = sql.trim().substring(0, 6).toUpperCase();
        if (sqlPrefix.startsWith("SELECT")) {
            return SELECT;
        } else if (sqlPrefix.startsWith("UPDATE")) {
            return UPDATE;
        } else if (sqlPrefix.startsWith("INSERT")) {
            return INSERT;
        } else if (sqlPrefix.startsWith("DELETE")) {
            return DELETE;
        }
        return UNKNOWN;
    }
}
