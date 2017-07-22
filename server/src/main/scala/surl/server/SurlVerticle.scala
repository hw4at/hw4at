package surl.server

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router

class SurlVerticle extends ScalaVerticle {
  println("SurlVerticle is created!")

  override def start(): Unit = {
    println("SurlVerticle is called!")
    val router = Router.router(vertx)
    router.get("/echo/:msg").handler(context => new EchoHandler)
    vertx.createHttpServer().requestHandler(router.accept).listenFuture(9988, "0.0.0.0")
  }
}
