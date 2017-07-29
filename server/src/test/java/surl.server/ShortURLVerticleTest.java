package surl.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Component Test
 */
@RunWith(VertxUnitRunner.class)
public class ShortURLVerticleTest {

    private static DBAdapterProxy db;

    private static Vertx vertx;
    private static HttpClient client;

    @BeforeClass
    public static void before() throws IOException {
        db = new DBAdapterProxy();
        ShortURLVerticle shortURLVerticle = new ShortURLVerticle() {
            @Override
            protected DBService createDBService() throws IOException {
                return new DBService(db);
            }
        };

        vertx = Vertx.vertx();
        vertx.deployVerticle(shortURLVerticle);
        client = vertx.createHttpClient();
    }

    @AfterClass
    public static void after() {
        client.close();
        vertx.close();
    }

    @Test
    public void testEcho(TestContext context) {
        testGET(context, "/echo/hello", "hello", ShortURLServer.OK_CODE, "test echo");
    }

    @Test
    public void testHealthy(TestContext context) {
        testGET(context, "/health", null, ShortURLServer.OK_CODE, "test health");
    }

    @Test
    public void testNotHealthy(TestContext context) {
        db.connectHandler = (err, con) -> err.accept("db is down", null);
        testGET(context, "/health", null, ShortURLServer.ERROR_CODE, "test not health");
    }

    private void testGET(TestContext context, String url, String resBody, Integer statusCode, String desc) {
        Async async = context.async();
        client.getNow(ConfigurationService.config.getServerPort(), "localhost", url, res -> {
            res.bodyHandler(body -> {
                try {
                    if (statusCode != null) {
                        context.assertEquals(statusCode, res.statusCode(), desc);
                    }
                    if (resBody != null) {
                        context.assertEquals(resBody, body.toString(), desc);
                    }
                } finally {
                    db.reset();
                    async.complete();
                }
            });
        });
    }
}