package surl.server;

import io.vertx.core.Future;
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

        serverAdapter.onGet(router, "/create", this::createBookmark);
        serverAdapter.onGet(router, "/all", ctx -> controller.getAllBookmarks(null, errorHandler(ctx), jsonHandler(ctx)));
        serverAdapter.onGet(router, "/all", ctx -> controller.getAllBookmarks(serverAdapter.getParam(ctx, "user"), errorHandler(ctx), jsonHandler(ctx)));

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

    protected void createBookmark(RoutingContext ctx) {
        try {
            JsonObject json = serverAdapter.getBodyAsJson(ctx);
            controller.createBookmark(json, errorHandler(ctx), okHandler(ctx));
        } catch(Exception e) {
            serverAdapter.respond(ctx, ERROR_CODE, "Invalid input");
        }
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

    private Consumer<String> jsonHandler(RoutingContext ctx) {
        return res -> serverAdapter.respondAsJson(ctx, OK_CODE, res);
    }

    private BiConsumer<String, Throwable> errorHandler(RoutingContext ctx) {
        return (msg, e) -> serverAdapter.respond(ctx, ERROR_CODE, msg);
    }

    private Utils.Done okHandler(RoutingContext ctx) {
        return () -> serverAdapter.respond(ctx, OK_CODE, OK_BODY);
    }
}
