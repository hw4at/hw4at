package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import surl.server.services.*;

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

        initServices(vertx, startFuture);

        router.get("/echo/:msg").handler(ctx -> ctx.response().end(ctx.request().getParam("msg")));

        router.get("/health").handler(ctx -> {
            logger.debug("Health check ...");
            Future<?> dbTest = Future.future();
            dbTest.setHandler(res -> {
                if (res.succeeded()) {
                    logger.debug("Healthy");
                    ctx.response().end("Ok");
                } else {
                    ctx.response().setStatusCode(500).end("Not healthy, check the logs");
                }
            });
            DBServiceHolder.db.testConnection(dbTest);
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigurationHolder.config.getServerPort());
    }

    protected void initServices(Vertx vertx, Future<Void> startFuture) throws IOException {
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/surl.properties")) {
            prop.load(in);
        }
        ConfigurationHolder.config = new ConfigurationService(prop);
        DBServiceHolder.db = new DBService(new SQLClientFactory().createMySQLClient(vertx));
        DBServiceHolder.db.testConnection(startFuture);

        Future<?> dbTest = Future.future();
        dbTest.setHandler(res -> {
            if (res.succeeded()) {
                logger.info("Server is up and ready ...");
                startFuture.complete();
            } else {
                logger.error("Server failed to start", res.cause());
                if (res.cause() !=null) {
                    startFuture.fail(res.cause());
                } else {
                    startFuture.fail("Server failed to start, check more details in the log");
                }
            }
        });
        DBServiceHolder.db.testConnection(dbTest);
    }
}
