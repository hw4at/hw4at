package surl.server.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import surl.server.ShortURLException;

public class DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private static final String DEFAULT_USER = "def";

    private SQLClient client;

    public static class Bookmark {
        public String user, name, url;
    }

    public DBService(SQLClient client) {
        this.client = client;
    }

    public void testConnection(Future<?> future) {
        client.getConnection(res -> {
            if (res.succeeded()) {
                future.complete();
            } else {
                logger.error("Unable to connect the DB", res.cause());
                future.fail(new ShortURLException("Unable to connect the DB", res.cause()));
            }
        });
    }

    public void newBookmark(Bookmark bookmark, Handler<String> handler) {
    }

    public void allBookmarks(String user, Handler<JsonArray> handler) {
        String actualUser = user!=null ? user : DEFAULT_USER;
        client.getConnection(conRes -> {
            if (conRes.succeeded()) {
                //TODO SQL Injection
                conRes.result().query("select name, short_url, full_url from bookmarks where user = '" + actualUser +"'", qryRes -> {
                    if (qryRes.succeeded()) {
                        JsonArray array = new JsonArray();
                        qryRes.result().getRows().forEach(r -> array.add(r));
                        handler.handle(array);
                    } else {
                        logger.error("Failed to get all bookmarks for user: " + actualUser, qryRes.cause());
                        handler.handle(new JsonArray());
                    }
                });
            } else {
                logger.error("Failed to get connection", conRes.cause());
                handler.handle(new JsonArray());
            }
        });
    }
}
