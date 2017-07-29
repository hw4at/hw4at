package surl.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.IOException;

public class ShortURLVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ShortURLVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        ConfigurationService.init();
        DBService db = createDBService();
        new ShortURLServer(new ServerAdapterImpl(), new ServerController(db), db).start(vertx, startFuture);
    }

    protected DBService createDBService() throws IOException {
        return new DBService(new DBAdapterFactory().createDBAdapter(vertx));
    }
}
