package surl.server

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class EchoHandler extends Handler[RoutingContext] {
  println("EchoHandler is created!")

  override def handle(context: RoutingContext): Unit = {
    val message: String = context.request.getParam("msg")
    context.response.setStatusCode(200)
    if (message != null && !message.isEmpty) {
      context.response.end(message)
    } else {
      context.response.end()
    }
  }
}
