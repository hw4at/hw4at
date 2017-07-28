package surl.server;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;

public interface DBService {
    static class Bookmark {
        public String user, name, url;
    }

    void testConnection(Future<?> future);

    void newBookmark(Bookmark bookmark, Handler<String> handler);

    void allBookmarks(String user, Handler<JsonArray> handler);
}
