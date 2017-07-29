package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static surl.server.Utils.TrioHandler;
import static org.mockito.Mockito.mock;

public class DBAdapterMock implements DBAdapter {

    public static final SQLConnection conn = mock(SQLConnection.class);

    public static final Consumer<TrioHandler<SQLConnection>> okConnectionHandler = handler(false, null);
    public static final BiConsumer<String, TrioHandler<List<JsonObject>>> emptyQuery = handler(false, null, Collections.emptyList());

    public Consumer<TrioHandler<SQLConnection>> connectionHandler;
    public BiConsumer<String, TrioHandler<List<JsonObject>>> queryHandler;

    public DBAdapterMock() {
        reset();
    }

    public void reset() {
        connectionHandler = okConnectionHandler;
        queryHandler = emptyQuery;
    }

    public static Consumer<TrioHandler<SQLConnection>> handler(boolean failed, String err) {
        return h -> h.accept(failed, failed ? new Exception(err) : null, failed ? null : conn);
    }

    public static BiConsumer<String, TrioHandler<List<JsonObject>>> handler(boolean failed, Throwable err, List<JsonObject> res) {
        return (query, handler) -> handler.accept(failed, err, res);
    }

    @Override
    public void connect(TrioHandler<SQLConnection> handler) {
        connectionHandler.accept(handler);
    }

    @Override
    public void query(SQLConnection con, String query, TrioHandler<List<JsonObject>> handler) {
        queryHandler.accept(query, handler);
    }
}
