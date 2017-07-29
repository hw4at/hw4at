package surl.server;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public class ShortURLServer {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLServer.class);

    public static final String OK_BODY = "Ok";
    public static final int OK_CODE = 200, ERROR_CODE = 500;

    private ServerAdapter serverAdapter;

    public void run(Vertx vertx, Future<Void> startFuture, ServerAdapter serverAdapter) throws IOException {
        this.serverAdapter = serverAdapter;

        logger.debug("Server is starting ...");
        Router router = serverAdapter.router(vertx);

        serverAdapter.onError(router, t -> {
            t.printStackTrace();
            logger.error(t.getMessage(), t);
        });

        serverAdapter.onGet(router, "/echo/:msg", ctx -> serverAdapter.respond(ctx, OK_CODE, serverAdapter.param(ctx, "msg")));
        serverAdapter.onGet(router, "/health", this::handleHealth);

        serverAdapter.onGet(router, "/new", this::handleNewBookmark);
        serverAdapter.onGet(router, "/all/:user", ctx -> handleAllBookmarks(ctx, serverAdapter.param(ctx, "user")));
        serverAdapter.onGet(router, "/all", ctx -> handleAllBookmarks(ctx, null));

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
        DBServiceHolder.db.testConnection(dbTest);
    }

    protected void handleNewBookmark(RoutingContext ctx) {
        try {
            JsonObject json = serverAdapter.body(ctx);
            DBServiceHolder.db.newBookmark(json, (res, err) -> {
                serverAdapter.respond(ctx, err == null ? OK_CODE : ERROR_CODE, err == null ? OK_BODY : err);
            });
        } catch(Exception e) {
            serverAdapter.respond(ctx, ERROR_CODE, "Invalid input");
        }
    }

    protected void handleAllBookmarks(RoutingContext ctx, String user) {
        DBServiceHolder.db.allBookmarks(user, (res, err) -> {
            if (err != null) {
                serverAdapter.respond(ctx, ERROR_CODE, err);
            } else {
                serverAdapter.respond(ctx, OK_CODE, res);
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
        DBServiceHolder.db.testConnection(dbTest);
    }
}
