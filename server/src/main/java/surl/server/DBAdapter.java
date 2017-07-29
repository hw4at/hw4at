package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import static surl.server.Utils.TrioHandler;

public interface DBAdapter {

    void connect(TrioHandler<SQLConnection> handler);

    void query(SQLConnection con, String query, TrioHandler<List<JsonObject>> handler);
}
