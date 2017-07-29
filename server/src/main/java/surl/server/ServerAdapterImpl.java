package surl.server;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ServerAdapterImpl implements ServerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerAdapterImpl.class);

    @Override
    public void onGet(Router router, String url, Handler<RoutingContext> handler) {
        router.get(url).handler(handler);
    }

    @Override
    public String param(RoutingContext ctx, String name) {
        return ctx.request().getParam(name);
    }

    @Override
    public JsonObject body(RoutingContext ctx) {
        return ctx.getBodyAsJson();
    }

    @Override
    public void respond(RoutingContext ctx, int statusCode, String body) {
        if (Utils.isEmpty(body)) {
            ctx.response().setStatusCode(statusCode).end();
        } else {
            ctx.response().setStatusCode(statusCode).end(body);
        }
    }

    @Override
    public void respond(RoutingContext ctx, int statusCode, JsonObject body) {
        ctx.response().putHeader("content-type", "application/json").end(body.encodePrettily());
    }

    @Override
    public void respond(RoutingContext ctx, int statusCode, JsonArray body) {
        ctx.response().putHeader("content-type", "application/json").end(body.encodePrettily());
    }
}
