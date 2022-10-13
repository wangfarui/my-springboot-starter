package com.wfr.springboot.base.dao.context;

/**
 * sql key常量类
 *
 * @author wangfarui
 * @since 2022/7/26
 */
public abstract class SqlConstants {

    /**
     * @see com.wfr.springboot.base.dao.context.SqlMethod
     */
    public static final String SQL_METHOD = "sql_method";

    /**
     * sql语句 (完整内容)
     */
    public static final String SQL_CONTENT = "sql_content";

    /**
     * sql参数 (在 SQL_CONTENT 不完整的情况下赋值)
     */
    public static final String SQL_PARAM = "sql_param";

}
