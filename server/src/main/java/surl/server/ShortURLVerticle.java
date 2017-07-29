package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class ShortURLVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.debug("Server is starting ...");

        ConfigurationServiceHolder.init();
        DBServiceHolder.init(new DBAdapterFactory().createDBAdapter(vertx));

        Router router = Router.router(vertx);

        router.exceptionHandler(t -> {
            t.printStackTrace();
            logger.error(t.getMessage(), t);
        });

        Server server = new Server(router, new ServerAdapterImpl());
        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigurationServiceHolder.config.getServerPort());
        server.ready(startFuture);
    }
}
