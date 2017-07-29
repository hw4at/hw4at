package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

public interface DBAdapter {
    interface TrioConsumer<T1, T2, T3> {
        void accept(T1 p1, T2 p2, T3 p3);
    }

    interface TrioHandler<T> extends TrioConsumer<Boolean, Throwable, T> {
    }

    void connect(TrioHandler<SQLConnection> handler);

    void query(SQLConnection con, String query, TrioHandler<List<JsonObject>> handler);
}
