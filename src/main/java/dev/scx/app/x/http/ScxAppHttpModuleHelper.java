package dev.scx.app.x.http;

import dev.scx.http.routing.request_matcher.TypeIsRequestMatcher;
import dev.scx.http.routing.route_table.PriorityRouteEntry;
import dev.scx.websocket.x.ScxServerWebSocketHandshakeRequest;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;

import static java.net.NetworkInterface.networkInterfaces;

/// ScxAppHttpModuleHelper
///
/// @author scx567888
/// @version 0.0.1
final class ScxAppHttpModuleHelper {

    /// 获取本机的 IP 地址 (不包括回环地址)
    ///
    /// @return 本机的 IP 地址
    public static InetAddress[] getLocalIPAddress() throws SocketException {
        return networkInterfaces()
            .flatMap(NetworkInterface::inetAddresses)
            .filter(c -> !c.isLoopbackAddress() && c instanceof Inet4Address) // 过滤本机和非 ipv4 地址
            .toArray(InetAddress[]::new);
    }

    public static List<PriorityRouteEntry> filterHttpRoutes(List<PriorityRouteEntry> routeEntries) {
        // 所有不是 WebSocket 的我们都认为是 http 路由
        return routeEntries.stream().filter(r -> {
                var requestMatcher = r.route().requestMatcher();
                if (requestMatcher instanceof TypeIsRequestMatcher t) {
                    return t.requestType() != ScxServerWebSocketHandshakeRequest.class;
                }
                return true;
            }
        ).toList();
    }

    public static List<PriorityRouteEntry> filterWebSocketRoutes(List<PriorityRouteEntry> routeEntries) {
        return routeEntries.stream().filter(r -> {
                var requestMatcher = r.route().requestMatcher();
                if (requestMatcher instanceof TypeIsRequestMatcher t) {
                    return t.requestType() == ScxServerWebSocketHandshakeRequest.class;
                }
                return false;
            }
        ).toList();
    }

}
