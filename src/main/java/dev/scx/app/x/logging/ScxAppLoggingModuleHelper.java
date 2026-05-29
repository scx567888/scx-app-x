package dev.scx.app.x.logging;

import static java.lang.System.Logger.Level.*;

final class ScxAppLoggingModuleHelper {

    public static System.Logger.Level toLevel(String level) {
        if (level == null) {
            throw new NullPointerException("level 不能为空 !!!");
        }
        level = level.trim().toUpperCase();
        return switch (level) {
            case "OFF", "O" -> OFF;
            case "ERROR", "E" -> ERROR;
            case "WARN", "WARNING", "W" -> WARNING;
            case "INFO", "I" -> INFO;
            case "DEBUG", "D" -> DEBUG;
            case "TRACE", "T" -> TRACE;
            case "ALL", "A" -> ALL;
            default -> null;
        };
    }

    public static LoggingType toType(String type) {
        if (type == null) {
            throw new NullPointerException("type 不能为空 !!!");
        }
        type = type.trim().toUpperCase();
        return switch (type) {
            case "CONSOLE", "C" -> LoggingType.CONSOLE;
            case "FILE", "F" -> LoggingType.FILE;
            case "BOTH", "B" -> LoggingType.BOTH;
            default -> null;
        };
    }

}
