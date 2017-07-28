package surl.server;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DBServiceMock implements  DBService {

    public static final Consumer<Future<?>> complete = f -> f.complete();
    public static final BiConsumer<String, Handler<JsonArray>> noBookmarks = (u, h) -> h.handle(new JsonArray());

    public final Map<String, List<Bookmark>> db = new HashMap<>();

    public Consumer<Future<?>> testConnection;
    public BiConsumer<String, Handler<JsonArray>> allBookmarks;

    public DBServiceMock() {
        reset();
        System.out.println("DBServiceMock is created");
    }

    public void reset() {
        System.out.println("reset is called");
        db.clear();
        testConnection = complete;
        allBookmarks = noBookmarks;
    }

    @Override
    public void testConnection(Future<?> future) {
        testConnection.accept(future);
    }

    @Override
    public void newBookmark(Bookmark bookmark, Handler<String> handler) {

    }

    @Override
    public void allBookmarks(String user, Handler<JsonArray> handler) {
        allBookmarks.accept(user, handler);
    }


}
