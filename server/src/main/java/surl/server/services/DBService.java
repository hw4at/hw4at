package surl.server.services;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;

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
            if (res.failed()) {
                logger.error("Unable to connect the DB", res.cause());
                if (res.cause() != null) {
                    future.fail(res.cause());
                } else {
                    future.fail("Unable to connect the DB");
                }
            } else {
                logger.debug("testConnection is passed");
                future.complete();
            }
        });
    }
}
