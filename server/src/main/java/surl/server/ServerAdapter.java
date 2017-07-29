package surl.server;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface ServerAdapter {

    void onGet(Router router, String url, Handler<RoutingContext> handler);

    String param(RoutingContext ctx, String name);

    JsonObject body(RoutingContext ctx);

    void respond(RoutingContext ctx, int statusCode, String body);

    void respond(RoutingContext ctx, int statusCode, JsonObject body);

    void respond(RoutingContext ctx, int statusCode, JsonArray body);
}
