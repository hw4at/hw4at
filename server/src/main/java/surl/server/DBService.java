package surl.server;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static surl.server.Utils.ErrCode;

public class DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private static final String NEW_BOOKMARKS_SQL = "INSERT INTO bookmarks (user, name, short_url, full_url) VALUES ('%s', '%s', '%s', '%s');";
    private static final String ALL_BOOKMARKS_SQL = "SELECT name, short_url, full_url FROM bookmarks";
    private static final String ALL_USER_BOOKMARKS_SQL = "SELECT name, short_url, full_url FROM bookmarks WHERE user = '%s'";

    private DBAdapter adapter;

    public DBService(DBAdapter adapter) {
        this.adapter = adapter;
    }

    public void testConnection(Future<?> future) {
        adapter.connect((msg, e) -> future.fail(msg), h -> future.complete());
    }

    public void createBookmark(String user, String name, String shortUrl, String fullUrl, BiConsumer<String, Throwable> errHandler, Consumer<Integer> resHandler) {
        String sql = String.format(NEW_BOOKMARKS_SQL, user, name, shortUrl, fullUrl);
        adapter.connect(errHandler, con -> adapter.update(con, sql, errHandler, resHandler));
    }

    public void getAllBookmarks(String user, BiConsumer<String, Throwable> errHandler, Consumer<JsonArray> resHandler) {
        String sql = null;
        if (Utils.isEmpty(user)) {
            sql = ALL_BOOKMARKS_SQL;
        } else {
            sql = String.format(ALL_USER_BOOKMARKS_SQL, user);
        }

        String actualSql = sql;
        adapter.connect(errHandler, con -> {
            adapter.query(con, actualSql, errHandler, res -> {
                JsonArray array = new JsonArray();
                res.forEach(r -> array.add(r));
                resHandler.accept(array);
            });
        });
    }

}
