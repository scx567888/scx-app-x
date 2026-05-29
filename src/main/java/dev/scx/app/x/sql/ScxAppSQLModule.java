package dev.scx.app.x.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.x.sql.handler.ObjectSQLHandlerFactory;
import dev.scx.jdbc.spy.ScxJdbcSpy;
import dev.scx.jdbc.spy.listener.logging.LoggingDataSourceListener;
import dev.scx.jdbc.spy.listener.logging.PreparedStatementLogStyle;
import dev.scx.sql.JDBCConnectionInfo;
import dev.scx.sql.SQLClient;
import dev.scx.sql.TypeSQLResolver;

/// ScxAppSQLModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppSQLModule implements ScxAppModule {

    private SQLClient sqlClient;

    private static SQLClient initSQLClient(ScxEnvironment environment) {
        var dataSourceUrl = environment.get("scx.sql.url", String.class);
        var dataSourceUsername = environment.get("scx.sql.username", String.class);
        var dataSourcePassword = environment.get("scx.sql.password", String.class);
        var dataSourceParameters = environment.get("scx.sql.parameters", String[].class, "[]");

        var jdbcConnectionInfo = new JDBCConnectionInfo(
            dataSourceUrl,
            dataSourceUsername,
            dataSourcePassword,
            dataSourceParameters
        );

        // 这里额外添加一个 处理 json 的 handler
        var typeSQLResolver = TypeSQLResolver.builder()
            .registerDefaultHandlers()
            .registerHandlerFactory(new ObjectSQLHandlerFactory())
            .build();

        var useSpy = environment.get("scx.sql.use-spy", boolean.class, false);

        return SQLClient.of(
            jdbcConnectionInfo,
            typeSQLResolver,
            d -> {
                var hikariConfig = new HikariConfig();
                hikariConfig.setDataSource(d);
                return new HikariDataSource(hikariConfig);
            },
            d -> useSpy ?
                ScxJdbcSpy.spy(d, new LoggingDataSourceListener(PreparedStatementLogStyle.RENDERED_SQL)) :
                d
        );

    }

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        this.sqlClient = initSQLClient(environment);
        // 把 sqlClient 注入到 容器中
        return ScxAppModuleDefinition.of()
            .componentInstance(this.sqlClient);
    }

    /// 暴漏 sqlClient, 方便外部使用
    public SQLClient sqlClient() {
        return this.sqlClient;
    }

}
