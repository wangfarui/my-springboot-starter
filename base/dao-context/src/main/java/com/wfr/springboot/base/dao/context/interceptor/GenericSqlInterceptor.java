package com.wfr.springboot.base.dao.context.interceptor;

import com.wfr.springboot.base.dao.context.SqlInterceptor;
import com.wfr.springboot.base.dao.context.SqlConstants;
import com.wfr.springboot.base.dao.context.SqlMethod;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.Logger;
import org.springframework.lang.Nullable;

/**
 * SQL通用拦截器
 *
 * @author wangfarui
 * @since 2022/10/13
 */
public class GenericSqlInterceptor implements SqlInterceptor {

    /**
     * 正常sql日志级别
     */
    protected final LogLever normalSqlLogLevel;

    /**
     * 慢sql日志级别
     */
    protected final LogLever slowSqlLogLevel;

    /**
     * 异常sql日志级别
     */
    protected final LogLever errorSqlLogLevel;

    /**
     * 慢sql时间
     */
    protected final long slowSqlTime;

    protected final boolean simplifySql;

    /**
     * 主日志服务
     */
    protected final LogService logService;

    private static final ThreadLocal<LogData> SQL_LOG_CONTEXT = new ThreadLocal<>();

    public GenericSqlInterceptor() {
        this(null);
    }

    public GenericSqlInterceptor(@Nullable DaoSqlLogProperties sqlLogProperties) {
        DaoSqlLogProperties properties = sqlLogProperties != null ? sqlLogProperties : new DaoSqlLogProperties();
        this.normalSqlLogLevel = properties.getNormalSqlLogLevel();
        this.slowSqlLogLevel = properties.getSlowSqlLogLevel();
        this.errorSqlLogLevel = properties.getErrorSqlLogLevel();
        this.slowSqlTime = properties.getSlowSqlTime().toMillis();
        this.simplifySql = properties.isSimplifySql();
        this.logService = Logger.defaultLogService();
    }

    /**
     * 创建sql日志
     *
     * @param sql sql语句
     * @return 日志数据
     */
    public LogData createLog(String sql) {
        LogData logData = LogData.createLogData(this.normalSqlLogLevel)
                .add(SqlConstants.SQL_METHOD, SqlMethod.getSqlMethod(sql))
                .add(SqlConstants.SQL_CONTENT, this.simplifySql ? simplifySqlContent(sql) : sql);
        SQL_LOG_CONTEXT.set(logData);
        return logData;
    }

    /**
     * 收集异常日志
     *
     * @param e      异常信息
     * @param isPush 是否立即推送日志
     */
    public void exceptionLog(Throwable e, boolean isPush) {
        LogData logData = SQL_LOG_CONTEXT.get();
        if (logData == null) {
            return;
        }
        logData.logLever(this.errorSqlLogLevel)
                .addException(e);
        if (isPush) {
            doPushLog(logData);
        }
    }

    /**
     * 推送日志
     */
    public void pushLog() {
        LogData logData = SQL_LOG_CONTEXT.get();
        if (logData == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - logData.getStartTime() > this.slowSqlTime) {
            logData.logLever(this.slowSqlLogLevel);
        }
        doPushLog(logData);
    }

    private void doPushLog(LogData logData) {
        this.logService.push(logData);
        SQL_LOG_CONTEXT.remove();
    }

    private String simplifySqlContent(String sql) {
        return sql.replaceAll("[\\s]+", " ").trim();
    }
}
