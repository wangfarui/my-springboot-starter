package com.wfr.springboot.base.dao.context.mybatis;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.Logger;
import com.zaxxer.hikari.pool.ProxyStatement;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 基于 Mysql 数据源的SQL拦截器
 *
 * @author wangfarui
 * @since 2022/7/26
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class HikariDatasourceMyBatisSqlInterceptor extends MyBatisSqlInterceptor {

    private final Field delegate;

    public HikariDatasourceMyBatisSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        super(sqlLogProperties);
        Field delegateField = null;
        try {
            delegateField = ProxyStatement.class.getDeclaredField("delegate");
            delegateField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Logger.warn("com.zaxxer.hikari.pool.ProxyStatement的 delegate 字段不存在, 请检查Hikari版本");
        }
        this.delegate = delegateField;
    }

    @Override
    protected String getNativeSql(Object statement) throws SQLException {
        if (statement instanceof ProxyStatement && this.delegate != null) {
            try {
                Object o = this.delegate.get(statement);
                if (o instanceof ClientPreparedStatement) {
                    ClientPreparedStatement clientPreparedStatement = (ClientPreparedStatement) o;
                    return clientPreparedStatement.asSql();
                }
            } catch (IllegalAccessException e) {
                LogData.error().addException(e).addMessage("获取值异常").push();
            }
        }
        return null;
    }
}