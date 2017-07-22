package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
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
        Router router = Router.router(vertx);

        router.exceptionHandler(t -> {
            t.printStackTrace();
            logger.error(t.getMessage(), t);
        });

        initServices(vertx, startFuture);
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hi")).listen(ConfigurationHolder.config.getServerPort());
    }

    protected void initServices(Vertx vertx, Future<Void> startFuture) throws IOException {
        logger.debug("initServices is called");
        Properties prop = new Properties();
        InputStream in = new FileInputStream("src/main/resources/surl.properties");
        prop.load(in);
        in.close();
        ConfigurationHolder.config = new ConfigurationService(prop);
        DBServiceHolder.db = new DBService(new SQLClientFactory().createMySQLClient(vertx));
        DBServiceHolder.db.testConnection(startFuture);
    }
}
