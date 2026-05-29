package dev.scx.app.x.logging;

import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.environment.type.ConfiguredPath;
import dev.scx.logging.ScxLoggerConfig;
import dev.scx.logging.ScxLogging;
import dev.scx.logging.recorder.ConsoleRecorder;
import dev.scx.logging.recorder.FileRecorder;
import dev.scx.reflect.TypeReference;

import java.util.List;

import static dev.scx.app.module.logging.ScxAppLoggingModuleHelper.toLevel;
import static dev.scx.app.module.logging.ScxAppLoggingModuleHelper.toType;

/// ScxAppLoggingModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppLoggingModule implements ScxAppModule {

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {

        // 先初始化 默认日志格式
        var defaultLevel = toLevel(environment.get("scx.logging.default.level", String.class, "error"));
        var defaultType = toType(environment.get("scx.logging.default.type", String.class, "console"));
        var defaultPath = environment.get("scx.logging.default.path", ConfiguredPath.class, "AppRoot:logs");
        var defaultTrace = environment.get("scx.logging.default.trace", boolean.class, false);

        // 设置默认的 logging 这里我们先清除所有的 Recorders
        var defaultLoggerConfig = ScxLogging.rootConfig().clearRecorders();
        defaultLoggerConfig.setLevel(defaultLevel);
        if (defaultType == LoggingType.CONSOLE || defaultType == LoggingType.BOTH) {
            defaultLoggerConfig.addRecorder(new ConsoleRecorder());
        }
        if (defaultType == LoggingType.FILE || defaultType == LoggingType.BOTH) {
            defaultLoggerConfig.addRecorder(new FileRecorder(defaultPath.path()));
        }
        defaultLoggerConfig.setStackTrace(defaultTrace);

        // 设置具体的日志
        var loggers = environment.get("scx.logging.loggers", new TypeReference<List<LoggingConfig>>() {});

        if (loggers == null) {
            return ScxAppModuleDefinition.of();
        }

        for (var logger : loggers) {
            if (logger.name == null || logger.name.isBlank()) {
                continue;
            }
            var level = toLevel(logger.level);
            var type = toType(logger.type);
            var path = logger.path == null ? defaultPath : logger.path;
            var trace = logger.trace;

            var loggerConfig = new ScxLoggerConfig();
            loggerConfig.setLevel(level);
            if (type == LoggingType.CONSOLE || type == LoggingType.BOTH) {
                loggerConfig.addRecorder(new ConsoleRecorder());
            }
            if (type == LoggingType.FILE || type == LoggingType.BOTH) {
                //文件路径的缺省值使用 默认的
                loggerConfig.addRecorder(new FileRecorder(path.path()));
            }
            loggerConfig.setStackTrace(trace);

            ScxLogging.setConfig(logger.name, loggerConfig);
        }
        return ScxAppModuleDefinition.of();
    }

}
