package surl.server;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ShortURLServer {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLServer.class);

    public static final String OK_BODY = "Ok";
    public static final int OK_CODE = 200, ERROR_CODE = 500;

    public static final String URL_REDIRECT_PREFIX = "lk";

    private ServerAdapter serverAdapter;
    private ServerController controller;
    private DBService db;

    public ShortURLServer(ServerAdapter serverAdapter, ServerController controller, DBService db) {
        this.serverAdapter = serverAdapter;
        this.controller = controller;
        this.db = db;
    }

    public void start(Vertx vertx, Future<Void> startFuture) throws IOException {
        logger.debug("Server is starting ...");

        Router router = serverAdapter.createRouter(vertx);

        serverAdapter.onError(router, t -> {
            t.printStackTrace();
            logger.error(t.getMessage(), t);
        });

        serverAdapter.onGet(router, "/echo/:msg", ctx -> serverAdapter.respond(ctx, OK_CODE, serverAdapter.getParam(ctx, "msg")));
        serverAdapter.onGet(router, "/health", this::handleHealth);

        serverAdapter.onPost(router, "/create", ctx -> {
            JsonObject json = getBodyAsJson(ctx);
            if (json !=null) {
                controller.createBookmark(json, errorHandler(ctx), stringHandler(ctx));
            }
        });

        serverAdapter.onGet(router, "/all", ctx -> controller.getAllBookmarks(null, errorHandler(ctx), jsonHandler(ctx)));
        serverAdapter.onGet(router, "/all", ctx -> controller.getAllBookmarks(serverAdapter.getParam(ctx, "user"), errorHandler(ctx), jsonHandler(ctx)));

        serverAdapter.onGet(router, "/" + URL_REDIRECT_PREFIX + "/:shortUrl", this::redirect);

        serverAdapter.onPut(router, "/update", ctx -> {
            JsonObject json = getBodyAsJson(ctx);
            if (json !=null) {
                controller.updateBookmark(json, errorHandler(ctx), intHandler(ctx));
            }
        });

        serverAdapter.onDelete(router, "/delete", ctx -> {
            JsonObject json = getBodyAsJson(ctx);
            if (json !=null) {
                controller.deleteBookmark(json, errorHandler(ctx), intHandler(ctx));
            }
        });

        serverAdapter.start(vertx, router);

        ready(startFuture);
    }

    private void ready(Future future) {
        Future<?> dbTest = Future.future();
        dbTest.setHandler(res -> {
            if (res.succeeded()) {
                logger.info("*** Server is up and ready ***");
                future.complete();
            } else {
                logger.error("*** Server failed to start ***", res.cause());
                future.fail("Server failed to start, please check the log for more details");
            }
        });
        db.testConnection(dbTest);
    }

    protected void redirect(RoutingContext ctx) {
        controller.redirect(serverAdapter.getParam(ctx, "shortUrl"), errorHandler(ctx),
            fullUrl -> {
                if (Utils.isEmpty(fullUrl)) {
                    serverAdapter.respond(ctx, ERROR_CODE, "No such URL");
                } else {
                    serverAdapter.redirect(ctx, fullUrl);
                }
            });
    }

    protected void handleHealth(RoutingContext ctx) {
        logger.debug("Health check ...");
        Future<?> dbTest = Future.future();
        dbTest.setHandler(res -> {
            if (res.succeeded()) {
                logger.debug("Healthy");
                serverAdapter.respond(ctx, OK_CODE, "Healthy");
            } else {
                logger.debug("Injured badly");
                serverAdapter.respond(ctx, ERROR_CODE, "Not healthy, please check the logs for more details");
            }
        });
        db.testConnection(dbTest);
    }

    protected JsonObject getBodyAsJson(RoutingContext ctx) {
        try {
            return serverAdapter.getBodyAsJson(ctx);
        } catch(Exception e) {
            logger.debug("Invalid json request", e);
            serverAdapter.respond(ctx, ERROR_CODE, "Invalid input, make sure the json request is valid");
        }

        return null;
    }

    private Consumer<String> stringHandler(RoutingContext ctx) {
        return res -> serverAdapter.respond(ctx, OK_CODE, res);
    }

    private Consumer<Integer> intHandler(RoutingContext ctx) {
        return count -> {
            if (count > 0) {
                serverAdapter.respond(ctx, OK_CODE, OK_BODY);
            } else {
                serverAdapter.respond(ctx, ERROR_CODE, "User and name are not found");
            }
        };
    }

    private Consumer<String> jsonHandler(RoutingContext ctx) {
        return res -> serverAdapter.respondAsJson(ctx, OK_CODE, res);
    }

    private BiConsumer<String, Throwable> errorHandler(RoutingContext ctx) {
        return (msg, e) -> serverAdapter.respond(ctx, ERROR_CODE, msg);
    }
}
