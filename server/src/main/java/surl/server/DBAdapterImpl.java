package surl.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

public class DBAdapterImpl implements DBAdapter {

    private SQLClient client;

    public DBAdapterImpl(SQLClient client) {
        this.client = client;
    }

    @Override
    public void connect(TrioHandler<SQLConnection> handler) {
        client.getConnection(con -> {
            handler.accept(con.failed(), con.cause(), con.result());
        });
    }

    @Override
    public void query(SQLConnection con, String query, TrioHandler<List<JsonObject>> handler) {
        con.query(query, res -> {
            handler.accept(res.failed(), res.cause(), res.succeeded() ? res.result().getRows() : null);
        });
    }
}
