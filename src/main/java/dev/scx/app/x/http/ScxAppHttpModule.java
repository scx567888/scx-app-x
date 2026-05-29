package dev.scx.app.x.http;

import dev.scx.ansi.Ansi;
import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.environment.type.ConfiguredPath;
import dev.scx.app.environment.type.ConfiguredSize;
import dev.scx.http.ScxHttpServer;
import dev.scx.http.routing.Router;
import dev.scx.http.x.HttpServer;
import dev.scx.http.x.HttpServerOptions;
import dev.scx.http.x.error_handler.DefaultHttpServerErrorHandler;
import dev.scx.tcp.tls.TLS;
import dev.scx.websocket.x.WebSocketUpgradeRequestFactory;

import java.io.IOException;
import java.net.Inet4Address;

import static dev.scx.app.x.http.ScxAppHttpModuleHelper.*;

/// ScxAppHttpModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppHttpModule implements ScxAppModule {

    private HttpServerOptions httpServerOptions;
    private ScxHttpServer httpServer;
    private Router router;

    public ScxAppHttpModule() {

    }

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {

        var maxPayloadSize = environment.get("scx.http.max-payload-size", ConfiguredSize.class, "16MB");
        var useDevelopmentErrorPage = environment.get("scx.http.use-development-error-page", Boolean.class, false);

        var sslEnabled = environment.get("scx.http.ssl.enabled", Boolean.class, false);
        var sslPath = environment.get("scx.http.ssl.path", ConfiguredPath.class);
        var sslPassword = environment.get("scx.http.ssl.password", String.class);

        this.httpServerOptions = new HttpServerOptions();

        this.httpServerOptions.maxPayloadSize(maxPayloadSize.size());

        if (sslEnabled) {
            var tls = TLS.of(sslPath.path(), sslPassword);
            this.httpServerOptions.tls(tls);
        }

        // 添加一个 websocket 处理器
        this.httpServerOptions.addUpgradeRequestFactory(new WebSocketUpgradeRequestFactory());

        this.router = Router.of();

        this.httpServer = new HttpServer(this.httpServerOptions)
            .onRequest(this.router)
            .onError(new DefaultHttpServerErrorHandler(useDevelopmentErrorPage));

        return ScxAppModuleDefinition.of();
    }

    @Override
    public void start(ScxApp scxApp) throws IOException {
        var environment = scxApp.environment();

        var port = environment.get("scx.http.port", Integer.class, 8888);

        var routeEntries = this.router.routeTable().entries();

        var httpRoutes = filterHttpRoutes(routeEntries);
        var webSocketRoutes = filterWebSocketRoutes(routeEntries);

        Ansi.ansi()
            .brightGreen("已加载 " + httpRoutes.size() + " 个 HTTP 路由 !!!").ln()
            .brightBlue("已加载 " + webSocketRoutes.size() + " 个 WebSocket 路由 !!!")
            .println();

        this.httpServer.start(port);

        var sslEnabled = this.httpServerOptions.tls() != null;
        var httpOrHttps = sslEnabled ? "https" : "http";
        var realPort = this.httpServer.localAddress().getPort();

        var o = Ansi.ansi().green("HTTP 服务器启动成功 !!!").ln();

        o.green("> 本地: " + httpOrHttps + "://localhost:" + realPort + "/").ln();

        var normalIP = getLocalIPAddress();

        for (var ip : normalIP) {
            o.green("> 网络: " + httpOrHttps + "://" + ip.getHostAddress() + ":" + realPort + "/").ln();
        }

        o.print();
    }

    @Override
    public void stop(ScxApp scxApp) {
        if (this.httpServer != null) {
            this.httpServer.stop();
        }
    }

    public ScxHttpServer httpServer() {
        return httpServer;
    }

    /// 暴漏 router 允许其他模块动态添加路由
    public Router router() {
        return router;
    }

}
