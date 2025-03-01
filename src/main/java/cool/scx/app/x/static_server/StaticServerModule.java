package cool.scx.app.x.static_server;

import cool.scx.app.Scx;
import cool.scx.app.ScxModule;
import cool.scx.http.routing.Router;
import cool.scx.http.routing.handler.StaticHandler;

import java.lang.System.Logger;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.DEBUG;


/**
 * StaticServerModule
 *
 * @author scx567888
 * @version 0.0.1
 */
public class StaticServerModule extends ScxModule {

    private static final Logger logger = System.getLogger(StaticServerModule.class.getName());

    private static void registerStaticServerHandler(Router router, List<StaticServer> staticServers) {
        for (var staticServer : staticServers) {
            router.route()
                    .path(staticServer.location())
                    .handler(new StaticHandler(staticServer.root()));
        }
    }


    @Override
    public void start(Scx scx) {
        var staticServers = scx.scxConfig().get("static-servers", new ConvertStaticServerHandler(scx.scxEnvironment()));
        logger.log(DEBUG, "静态资源服务器 -->  {0}", staticServers.stream().map(StaticServer::location).collect(Collectors.joining(", ", "[", "]")));
        registerStaticServerHandler(scx.scxHttpRouter(), staticServers);
    }

    @Override
    public String name() {
        return "SCX_EXT-" + super.name();
    }

}
