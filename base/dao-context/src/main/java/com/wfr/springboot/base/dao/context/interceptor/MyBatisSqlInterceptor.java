package com.wfr.springboot.base.dao.context.interceptor;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.wfr.base.framework.common.utils.StringUtils;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.Logger;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.sql.SQLException;

/**
 * MyBatis SQL拦截器
 * <p>主要拦截 {@code insert}、{@code delete}、{@code update}、{@code select} 语句</p>
 * <p>兼容Druid连接池</p>
 *
 * @author wangfarui
 * @since 2022/7/26
 */
public abstract class MyBatisSqlInterceptor extends GenericSqlInterceptor implements Interceptor {

    private static boolean dependDruidDataSource;

    static {
        try {
            Class.forName("com.alibaba.druid.pool.DruidPooledPreparedStatement");
            dependDruidDataSource = true;
        } catch (ClassNotFoundException e) {
            Logger.debug("No dependency on druid connection pools");
        }
    }

    public MyBatisSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        super(sqlLogProperties);
    }

    /**
     * 获取原生sql语句
     *
     * @param statement SQL Statement
     * @return sql
     */
    protected abstract String getNativeSql(Object statement) throws SQLException;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        try {
            Object statement = args[0];
            if (dependDruidDataSource) {
                if (statement instanceof DruidPooledPreparedStatement) {
                    statement = ((DruidPooledPreparedStatement) statement).getRawPreparedStatement();
                }
            }
            String nativeSql = getNativeSql(statement);
            if (StringUtils.isBlank(nativeSql)) {
                return invocation.proceed();
            }
            createLog(nativeSql);
            return invocation.proceed();
        } catch (Throwable e) {
            exceptionLog(e, false);
            throw e;
        } finally {
            pushLog();
        }
    }
}