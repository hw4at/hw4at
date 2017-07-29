package surl.server;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;

public class DBAdapterProxy implements DBAdapter {

    public static final SQLConnection conn = mock(SQLConnection.class);

    public interface ConnectHandler {
        void connect(BiConsumer<String, Throwable> errHandler, Consumer<SQLConnection> conHandler);
    }

    public interface QueryHandler {
        void query(String query, BiConsumer<String, Throwable> errHandler, Consumer<List<JsonObject>> resHandler);
    }

    public interface UpdateHandler {
        void update(String sql, BiConsumer<String, Throwable> errHandler, Consumer<Integer> resHandle);
    }

    public static final ConnectHandler CONNECT_HANDLER = (err, con) -> con.accept(conn);
    public static final QueryHandler QUERY_HANDLER = (query, err, res) -> res.accept(Collections.emptyList());
    public static final UpdateHandler UPDATE_HANDLER = (query, err, res) -> res.accept(0);

    public ConnectHandler connectHandler = null;
    public QueryHandler queryHandler = null;
    public UpdateHandler updateHandler = null;

    public DBAdapterProxy() {
        reset();
    }

    public void reset() {
        connectHandler = CONNECT_HANDLER;
        queryHandler = QUERY_HANDLER;
        updateHandler = UPDATE_HANDLER;
    }

    @Override
    public void connect(BiConsumer<String, Throwable> errHandler, Consumer<SQLConnection> conHandler) {
        connectHandler.connect(errHandler, conHandler);
    }

    @Override
    public void query(SQLConnection con, String query, BiConsumer<String, Throwable> errHandler, Consumer<List<JsonObject>> resHandler) {
        queryHandler.query(query, errHandler, resHandler);
    }

    @Override
    public void update(SQLConnection con, String sql, BiConsumer<String, Throwable> errHandler, Consumer<Integer> resHandle) {
        updateHandler.update(sql, errHandler, resHandle);
    }
}
