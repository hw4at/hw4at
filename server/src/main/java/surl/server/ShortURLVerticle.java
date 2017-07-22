package surl.server;

import io.vertx.core.AbstractVerticle;

public class ShortURLVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hi")).listen(9988);
    }
}
