package surl.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

import java.io.IOException;

public class SQLClientFactory {

    public SQLClient createMySQLClient(Vertx vertx) throws IOException {
        return MySQLClient.createNonShared(vertx, getDBJsonObject());
    }

    protected JsonObject getDBJsonObject() throws IOException {
        return new JsonObject()
                .put("host", ConfigurationServiceHolder.config.getDBHost())
                .put("port", ConfigurationServiceHolder.config.getDBPort())
                .put("database", ConfigurationServiceHolder.config.getDBSchema())
                .put("username", ConfigurationServiceHolder.config.getDBUserName())
                .put("password", ConfigurationServiceHolder.config.getDBPassword());
    }
}
