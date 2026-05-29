package dev.scx.app.x.static_server;

import static dev.scx.app.x.static_server.StaticServerType.SINGLE_FILE;
import static dev.scx.app.x.static_server.StaticServerType.STATIC_FILES;

/// ScxAppStaticServerModuleHelper
///
/// @author scx567888
/// @version 0.0.1
final class ScxAppStaticServerModuleHelper {

    public static StaticServerType toType(String type) {
        if (type == null) {
            return STATIC_FILES;
        }
        type = type.trim().toUpperCase();
        return switch (type) {
            case "STATIC-FILES" -> STATIC_FILES;
            case "SINGLE-FILE" -> SINGLE_FILE;
            default -> throw new IllegalArgumentException("未知 static-server.type : " + type);
        };
    }

}
