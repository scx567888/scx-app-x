package dev.scx.app.x.static_server;

import dev.scx.ansi.Ansi;
import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.x.http.ScxAppHttpModule;
import dev.scx.http.routing.Route;
import dev.scx.http.routing.method_matcher.MethodMatcher;
import dev.scx.http.routing.path_matcher.PathMatcher;
import dev.scx.http.routing.request_matcher.RequestMatcher;
import dev.scx.http.routing.x.single_file.SingleFileHandler;
import dev.scx.http.routing.x.static_files.StaticFilesHandler;
import dev.scx.reflect.TypeReference;

import static dev.scx.app.x.static_server.ScxAppStaticServerModuleHelper.toType;
import static dev.scx.http.headers.HttpHeaderName.HOST;

/// ScxAppStaticServerModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppStaticServerModule implements ScxAppModule {

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        return ScxAppModuleDefinition.of()
            .require(ScxAppHttpModule.class)
            .startBefore(ScxAppHttpModule.class);
    }

    @Override
    public void start(ScxApp scxApp) throws Exception {
        var httpModule = scxApp.getComponent(ScxAppHttpModule.class);

        var router = httpModule.router();

        var environment = scxApp.environment();

        var staticServers = environment.get("scx.static-servers", new TypeReference<StaticServer[]>() {});

        if (staticServers == null) {
            return;
        }

        for (var staticServer : staticServers) {
            RequestMatcher requestMatcher;
            if (staticServer.host == null || staticServer.host.isBlank()) {
                requestMatcher = RequestMatcher.any();
            } else {
                requestMatcher = req -> staticServer.host.equals(req.getHeader(HOST));
            }
            var pathMatcher = PathMatcher.ofTemplate(staticServer.route);
            var type = toType(staticServer.type);
            Route route = switch (type) {
                case STATIC_FILES -> Route.of(
                    requestMatcher,
                    pathMatcher,
                    MethodMatcher.any(),
                    StaticFilesHandler.of(staticServer.path.path())
                );
                case SINGLE_FILE -> Route.of(
                    requestMatcher,
                    pathMatcher,
                    MethodMatcher.any(),
                    SingleFileHandler.of(staticServer.path.path())
                );
            };
            // 用一个较靠后的优先级
            router.route(999999, route);
        }

        // 打印一下

        var o = Ansi.ansi()
            .brightMagenta("已加载 " + staticServers.length + " 个 Static Server !!!")
            .ln();

        for (var staticServer : staticServers) {
            var type = toType(staticServer.type);
            var route = staticServer.route;
            var path = staticServer.path.path();

            o.brightCyan(type + ": ")
                .brightYellow(route)
                .defaultColor(" -> ")
                .brightGreen(path.toString())
                .ln();
        }

        o.print();

    }

}
