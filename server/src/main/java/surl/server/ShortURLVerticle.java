package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ShortURLVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        ConfigurationServiceHolder.init();

        if (DBServiceHolder.db == null) {
            DBServiceHolder.db = new DBService(new DBAdapterFactory().createDBAdapter(vertx));
        }

        new ShortURLServer().run(vertx, startFuture, new ServerAdapterImpl());
    }
}
