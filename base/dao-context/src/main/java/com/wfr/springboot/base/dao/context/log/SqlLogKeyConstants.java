package com.wfr.springboot.base.dao.context.log;

/**
 * mybatis sql日志key 常量类
 *
 * @author wangfarui
 * @since 2022/7/26
 */
public abstract class SqlLogKeyConstants {

    /**
     * @see SqlMethod
     */
    public static final String SQL_METHOD = "sql_method";

    /**
     * sql语句 (完整内容)
     */
    public static final String SQL_CONTENT = "sql_content";


    /**
     * sql方法类型
     */
    static enum SqlMethod {
        SELECT,
        UPDATE,
        DELETE,
        INSERT
    }
}
