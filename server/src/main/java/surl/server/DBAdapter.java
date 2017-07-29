package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DBAdapter {

    void connect(BiConsumer<String, Throwable> errHandler, Consumer<SQLConnection> conHandler);

    void query(SQLConnection con, String query, BiConsumer<String, Throwable> errHandler, Consumer<List<JsonObject>> resHandler);

    void update(SQLConnection con, String sql, BiConsumer<String, Throwable> errHandler, Consumer<Integer> resHandle);
}
