package surl.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;

import java.io.IOException;

public class DBAdapterFactory {

    public DBAdapter createDBAdapter(Vertx vertx) throws IOException {
        return new DBAdapterImpl(MySQLClient.createNonShared(vertx, getDBJsonObject()));
    }

    protected JsonObject getDBJsonObject() throws IOException {
        return new JsonObject()
                .put("host", ConfigurationService.config.getDBHost())
                .put("port", ConfigurationService.config.getDBPort())
                .put("database", ConfigurationService.config.getDBSchema())
                .put("username", ConfigurationService.config.getDBUserName())
                .put("password", ConfigurationService.config.getDBPassword());
    }
}
