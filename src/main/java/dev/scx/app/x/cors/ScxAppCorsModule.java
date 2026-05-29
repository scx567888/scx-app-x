package dev.scx.app.x.cors;

import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.x.http.ScxAppHttpModule;
import dev.scx.http.headers.HttpHeaderName;
import dev.scx.http.method.HttpMethod;
import dev.scx.http.routing.Route;
import dev.scx.http.routing.method_matcher.MethodMatcher;
import dev.scx.http.routing.path_matcher.PathMatcher;
import dev.scx.http.routing.x.cors.CorsHandler;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;
import dev.scx.http.routing.x.cors.allow_origin.AllowOrigin;
import dev.scx.http.routing.x.cors.expose_headers.ExposeHeaders;

import static dev.scx.http.headers.HttpHeaderName.*;
import static dev.scx.http.method.HttpMethod.*;

// todo 待处理
public final class ScxAppCorsModule implements ScxAppModule {

    private static final HttpMethod[] DEFAULT_ALLOWED_METHODS = new HttpMethod[]{GET, POST, OPTIONS, DELETE, PATCH, PUT};
    private static final HttpHeaderName[] DEFAULT_ALLOWED_HEADERS = new HttpHeaderName[]{ACCEPT, CONTENT_TYPE};
    private static final HttpHeaderName[] DEFAULT_EXPOSED_HEADERS = new HttpHeaderName[]{CONTENT_DISPOSITION};

    /// Cors handler
    private CorsHandler corsHandler;

    private static CorsHandler initCorsHandler(String allowedOriginPattern) {
        if (allowedOriginPattern == null) {
            allowedOriginPattern = "*";
        }
        return CorsHandler.of()
            .allowOrigin(allowedOriginPattern.equals("*") ? AllowOrigin.ofWildcard() : AllowOrigin.of(allowedOriginPattern))
            .allowHeaders(AllowHeaders.of(DEFAULT_ALLOWED_HEADERS))
            .allowMethods(AllowMethods.of(DEFAULT_ALLOWED_METHODS))
            .exposeHeaders(ExposeHeaders.of(DEFAULT_EXPOSED_HEADERS))
            .allowCredentials(false);
    }

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        var allowedOrigin = environment.get("scx.cors.allowed-origin", String.class, "*");

        // 设置基本的 handler
        this.corsHandler = initCorsHandler(allowedOrigin);

        return ScxAppModuleDefinition.of()
            .require(ScxAppHttpModule.class)
            .startBefore(ScxAppHttpModule.class);
    }

    @Override
    public void start(ScxApp scxApp) {
        var httpModule = scxApp.getComponent(ScxAppHttpModule.class);

        var router = httpModule.router();

        // 注册路由
        var corsHandlerRoute = Route.of(PathMatcher.any(), MethodMatcher.any(), corsHandler);

        // 使用较靠前的优先级
        router.route(-10000, corsHandlerRoute);
    }

    /// 暴漏 CorsHandler 允许外部动态修改.
    public CorsHandler corsHandler() {
        return corsHandler;
    }

}
