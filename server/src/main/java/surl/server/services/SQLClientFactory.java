package surl.server.services;

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
                .put("host", ConfigurationHolder.config.getDBHost())
                .put("port", ConfigurationHolder.config.getDBPort())
                .put("database", ConfigurationHolder.config.getDBSchema())
                .put("username", ConfigurationHolder.config.getDBUserName())
                .put("password", ConfigurationHolder.config.getDBPassword());
    }
}
