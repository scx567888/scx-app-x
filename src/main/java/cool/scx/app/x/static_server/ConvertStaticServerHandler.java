package cool.scx.app.x.static_server;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.config.ScxConfigValueHandler;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.handler.DefaultValueHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ConvertStaticServerHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
record ConvertStaticServerHandler(ScxEnvironment scxEnvironment) implements ScxConfigValueHandler<List<StaticServer>> {

    @Override
    public List<StaticServer> handle(String keyPath, JsonNode rawValue) {
        var arrayList = DefaultValueHandler.of(new ArrayList<Map<String, String>>()).handle(keyPath, rawValue);
        var tempList = new ArrayList<StaticServer>();
        for (var arg : arrayList) {
            try {
                tempList.add(new StaticServer(arg.get("location"), scxEnvironment.getPathByAppRoot(arg.get("root"))));
            } catch (Exception ignored) {

            }
        }
        return tempList;
    }

}
