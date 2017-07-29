package surl.server;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static surl.server.Utils.ErrCode;

public class DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private static final Pattern USER_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final String DEFAULT_USER = "def";

    private static final String ALL_BOOKMARKS_QUERY = "SELECT name, short_url, full_url FROM bookmarks WHERE user = '%s'";

    private DBAdapter adapter;

    public DBService(DBAdapter adapter) {
        this.adapter = adapter;
    }

    public void testConnection(Future<?> future) {
        adapter.connect((conFailed, conErr, conRes) -> {
            if (conFailed) {
                logger.error(ErrCode.E133 + " Unable to connect the DB", conErr);
                future.fail(new ShortURLException("Unable to connect the DB", conErr));
            } else {
                future.complete();
            }
        });
    }

    public void newBookmark(JsonObject bookmark, BiConsumer<JsonArray, String> handler) {
    }

    public void allBookmarks(String user, BiConsumer<JsonArray, String> handler) {
        if (Utils.isEmpty(user)) {
            user = DEFAULT_USER;
        } else if (!USER_PATTERN.matcher(user).matches()) {
            logger.info("Tried to get all bookmarks with invalid user name: " + user);
            handler.accept(null, "Invalid user name");
        }

        String actualUser = user;
        adapter.connect((conFailed, conErr, conRes) -> {
            if (conFailed || conRes == null) {
                logger.error(ErrCode.E135 + " Failed to get connection", conErr);
                handler.accept(null, ErrCode.E135.oops());
            } else {
                adapter.query(conRes, String.format(ALL_BOOKMARKS_QUERY, actualUser), (qryFailed, qryErr, qryRes) -> {
                    if (qryFailed || qryRes == null) {
                        logger.error(ErrCode.E134 + " Failed to get all bookmarks for user: " + actualUser, qryErr);
                        handler.accept(null, ErrCode.E134.oops());
                    } else {
                        JsonArray array = new JsonArray();
                        qryRes.forEach(r -> array.add(r));
                        handler.accept(array, null);
                    }
                });
            }
        });
    }
}
