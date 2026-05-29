package dev.scx.app.x.web;

import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.environment.type.ConfiguredPath;
import dev.scx.app.x.http.ScxAppHttpModule;
import dev.scx.app.x.web.template.TemplateEngine;
import dev.scx.app.x.web.template.TemplateReturnValueHandler;
import dev.scx.web.ScxWeb;
import dev.scx.web.annotation.Routes;

import java.util.ArrayList;

/// ScxAppWebModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppWebModule implements ScxAppModule {

    private ScxWeb scxWeb;

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        var templatePath = environment.get("scx.web.template.path", ConfiguredPath.class, "AppRoot:templates");

        this.scxWeb = new ScxWeb();
        this.scxWeb.addReturnValueHandler(new TemplateReturnValueHandler(new TemplateEngine(templatePath.path())));

        return ScxAppModuleDefinition.of()
            .componentSelector(c -> c.getAnnotation(Routes.class) != null)
            .require(ScxAppHttpModule.class)
            .startBefore(ScxAppHttpModule.class);
    }

    @Override
    public void start(ScxApp scxApp) {
        var httpModule = scxApp.getComponent(ScxAppHttpModule.class);

        var router = httpModule.router();

        var webRoutes = new ArrayList<>();

        // 过滤所有候选类 种的 webRoute
        for (var candidate : scxApp.candidates()) {
            if (candidate.getAnnotation(Routes.class) != null) {
                var component = scxApp.getComponent(candidate);
                webRoutes.add(component);
            }
        }

        // 创建 路由
        var routes = scxWeb.routes(webRoutes.toArray());
        for (var route : routes) {
            router.route(route.priority(), route);
        }
    }

    public ScxWeb scxWeb() {
        return scxWeb;
    }

}
