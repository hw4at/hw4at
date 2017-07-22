package surl.server.services;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import surl.server.ShortURLException;

public class DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private SQLClient client;

    public DBService(SQLClient client) {
        logger.debug("DBService is called");
        this.client = client;
    }

    public void testConnection(Future<Void> future) {
        logger.debug("testConnection is called");
        client.getConnection(res -> {
            if (res.succeeded()) {
                logger.debug("testConnection is passed");
                future.complete();
            } else {
                logger.error("Unable to connect the DB", res.cause());
                future.fail(new ShortURLException("Unable to connect the DB", res.cause()));
            }
        });
    }
}
