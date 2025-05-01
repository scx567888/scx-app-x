package cool.scx.app.x.redirect;

import cool.scx.ansi.Ansi;
import cool.scx.app.ScxApp;
import cool.scx.app.ScxAppModule;
import cool.scx.http.routing.Router;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.HttpServer;
import cool.scx.web.vo.Redirection;

import java.lang.System.Logger;

/**
 * 监听 80 端口并将所有 http 请求重定向 到 https
 *
 * @author scx567888
 * @version 0.0.1
 */
public class RedirectModule extends ScxAppModule {

    private static final Logger logger = System.getLogger(RedirectModule.class.getName());

    private final int port;

    public RedirectModule() {
        this(80);
    }

    public RedirectModule(int port) {
        this.port = port;
    }

    /**
     * 也可以直接以工具类的形式调用
     *
     * @param port a int
     */
    public static void startRedirect(int port) {
        var httpServer = new HttpServer();
        httpServer.onRequest(request -> {
            var oldURI = request.uri();
            var newURI = ScxURI.of(oldURI).scheme("https");
            Redirection.ofTemporary(newURI.encode(true)).handle(request.response());
        });

        try {
            httpServer.start(port);
            Ansi.ansi().brightMagenta("转发服务器启动成功 http -> https, 端口号 : " + httpServer.localAddress().getPort() + " !!!").println();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "转发服务器启动失败 !!! ", e);
        }
    }

    @Override
    public String name() {
        return "SCX_EXT-" + super.name();
    }

    @Override
    public void start(ScxApp scx) {
        //只有当开启 https 的时候才进行转发
        if (scx.scxOptions().isHttpsEnabled()) {
            startRedirect(this.port);
        }
    }

}
