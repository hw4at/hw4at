package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ServerController {
    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    private static final Pattern VALUE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    private static final String USER = "user", NAME = "name", URL = "url", SHORT_URL = "short_url", FULL_URL = "full_url";

    private DBService db;

    public ServerController(DBService db) {
        this.db = db;
    }

    public void createBookmark(JsonObject bookmark, BiConsumer<String, Throwable> errHandler, Consumer<String> resHandler) {
        String user = bookmark.getString(USER);
        String name = bookmark.getString(NAME);
        String fullUrl = bookmark.getString(URL);

        if (isNotValidValue(user) || isNotValidValue(name) || isNotValidURL(fullUrl)) {
            logger.info("Invalid create bookmark request. user = %s, name = %s, url = %s", user, name, fullUrl);
            errHandler.accept("Invalid values", null);
            return;
        }

        String shortUrl = "aa" + fullUrl.hashCode();
        db.createBookmark(user, name, shortUrl, fullUrl, errHandler, res -> resHandler.accept(shortUrl));
    }

    public void getAllBookmarks(String user, BiConsumer<String, Throwable> errHandler, Consumer<String> resHandler) {
        if (!Utils.isEmpty(user) && isNotValidValue(user)) {
            logger.info("Tried to get all user bookmarks with invalid user name: " + user);
            errHandler.accept("Invalid user name " + user, null);
            return;
        }
        db.getAllBookmarks(user, errHandler, res -> resHandler.accept(res.encodePrettily()));
    }

    protected boolean isNotValidValue(String val) {
        return Utils.isEmpty(val) || val.length() > 64 || !VALUE_PATTERN.matcher(val).matches();
    }

    protected boolean isNotValidURL(String url) {
        return Utils.isEmpty(url) || url.length() > 255 || !URL_VALIDATOR.isValid(url);
    }
}
