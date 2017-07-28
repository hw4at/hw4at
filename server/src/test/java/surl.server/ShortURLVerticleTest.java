package surl.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class ShortURLVerticleTest {

    private static final int OK = 200, ERROR = 500;
    private static DBServiceMock dbMock;

    private static Vertx vertx;
    private static HttpClient client;

    @BeforeClass
    public static void before() throws IOException {
        DBServiceHolder.db = dbMock = new DBServiceMock();
        vertx = Vertx.vertx();
        ShortURLVerticle shortURLVerticle = new ShortURLVerticle();
        shortURLVerticle.initServices(vertx);
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
        testGET(context, "/echo/hello", "hello", OK, "test echo");
    }

    @Test
    public void testHealthy(TestContext context) {
        testGET(context, "/health", "Healthy", OK, "test health");
    }

    @Test
    public void testNotHealthy(TestContext context) {
        dbMock.testConnection = f -> f.fail("db is down");
        testGET(context, "/health", null, ERROR, "test not health");
    }

    private void testGET(TestContext context, String url, String resBody, Integer statusCode, String desc) {
        Async async = context.async();
        client.getNow(ConfigurationServiceHolder.config.getServerPort(), "localhost", url, res -> {
            res.bodyHandler(body -> {
                try {
                    if (statusCode != null) {
                        context.assertEquals(statusCode, res.statusCode(), desc);
                    }
                    if (resBody != null) {
                        context.assertEquals(resBody, body.toString(), desc);
                    }
                } finally {
                    dbMock.reset();
                    async.complete();
                }
            });
        });
    }
}