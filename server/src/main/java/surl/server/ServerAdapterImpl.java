package surl.server;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import static io.vertx.ext.web.Router.router;

public class ServerAdapterImpl implements ServerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerAdapterImpl.class);

    @Override
    public Router createRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        return router;
    }

    @Override
    public void start(Vertx vertx, Router router) {
        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigurationService.config.getServerPort());
    }

    @Override
    public void onError(Router router, Handler<Throwable> handler) {
        router.exceptionHandler(handler);
    }

    @Override
    public void onGet(Router router, String url, Handler<RoutingContext> handler) {
        router.get(url).handler(handler);
    }

    @Override
    public void onPost(Router router, String url, Handler<RoutingContext> handler) {
        router.post(url).handler(handler);
    }

    @Override
    public void onPut(Router router, String url, Handler<RoutingContext> handler) {
        router.put(url).handler(handler);
    }

    @Override
    public void onDelete(Router router, String url, Handler<RoutingContext> handler) {
        router.delete(url).handler(handler);
    }

    @Override
    public String getParam(RoutingContext ctx, String name) {
        return ctx.request().getParam(name);
    }

    @Override
    public JsonObject getBodyAsJson(RoutingContext ctx) {
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
    public void respondAsJson(RoutingContext ctx, int statusCode, String body) {
        ctx.response().putHeader("content-type", "application/json").end(body);
    }

    @Override
    public void redirect(RoutingContext ctx, String url) {
        ctx.response().putHeader("location", url).setStatusCode(302).end();
    }
}
