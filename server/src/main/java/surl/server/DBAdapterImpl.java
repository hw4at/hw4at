package surl.server;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DBAdapterImpl implements DBAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DBAdapterImpl.class);

    private SQLClient client;

    public DBAdapterImpl(SQLClient client) {
        this.client = client;
    }

    @Override
    public void connect(BiConsumer<String, Throwable> errHandler, Consumer<SQLConnection> conHandler) {
        client.getConnection(con -> {
            if (con.failed()) {
                logger.error("Failed to get connection to the DB", con.cause());
                errHandler.accept(Utils.ErrCode.E133.oops(), con.cause());
            } else {
                conHandler.accept(con.result());
            }
        });
    }

    @Override
    public void query(SQLConnection con, String query, BiConsumer<String, Throwable> errHandler, Consumer<List<JsonObject>> resHandler) {
        con.query(query, res -> {
            if (res.failed()) {
                logger.error("Failed to execute query: " + query, res.cause());
                errHandler.accept(Utils.ErrCode.E134.oops(), res.cause());
            } else {
                resHandler.accept(res.result().getRows());
            }
        });
    }
}
