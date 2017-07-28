package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ShortURLVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.debug("Server is starting ...");

        Router router = Router.router(vertx);

        router.exceptionHandler(t -> {
            t.printStackTrace();
            logger.error(t.getMessage(), t);
        });

        initServices(vertx);

        router.get("/echo/:msg").handler(ctx -> ctx.response().end(ctx.request().getParam("msg")));
        router.get("/health").handler(this::handleHealth);
        router.get("/all/:user").handler(ctx -> handleAllBookmarks(ctx, ctx.request().getParam("user")));
        router.get("/all").handler(ctx -> handleAllBookmarks(ctx, null));
//        router.post("/new").handler(ctx -> handleAllBookmarks(ctx, null));

        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigurationServiceHolder.config.getServerPort());

        Future<?> dbTest = Future.future();
        dbTest.setHandler(res -> {
            if (res.succeeded()) {
                logger.info("Server is up and ready ...");
                startFuture.complete();
            } else {
                logger.error("Server failed to start", res.cause());
                startFuture.fail("Server failed to start, please check the log for more details");
            }
        });
        DBServiceHolder.db.testConnection(dbTest);
    }

    protected void handleNewBookmark(RoutingContext routingContext) {
        DBService.Bookmark bookmark = Json.decodeValue(routingContext.getBodyAsString(), DBService.Bookmark.class);
    }

    protected void handleAllBookmarks(RoutingContext routingContext, String user) {
        DBServiceHolder.db.allBookmarks(user, res -> {
            routingContext.response().putHeader("content-type", "application/json").end(res.encodePrettily());
        });
    }

    protected void handleHealth(RoutingContext ctx) {
        logger.debug("Health check ...");
        Future<?> dbTest = Future.future();
        dbTest.setHandler(res -> {
            if (res.succeeded()) {
                logger.debug("Healthy");
                ctx.response().end("Healthy");
            } else {
                ctx.response().setStatusCode(500).end("Not healthy, please check the logs for more details");
            }
        });
        DBServiceHolder.db.testConnection(dbTest);
    }

    protected void initServices(Vertx vertx) throws IOException {
        if (ConfigurationServiceHolder.config == null) {
            Properties prop = new Properties();
            try (InputStream in = new FileInputStream("src/main/resources/surl.properties")) {
                prop.load(in);
            }
            ConfigurationServiceHolder.config = new ConfigurationService(prop);
        }

        if (DBServiceHolder.db == null) {
            DBServiceHolder.db = new DBServiceImpl(new SQLClientFactory().createMySQLClient(vertx));
        }
    }
}
