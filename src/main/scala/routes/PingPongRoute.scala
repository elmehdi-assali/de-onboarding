package routes

import controllers.PingPongController
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{endpoint, stringBody}

import scala.concurrent.Future

class PingPongRoute(controller: PingPongController) extends Route {
  private val pongEndpoint = endpoint.get
    .in("ping")
    .out(stringBody)
    .serverLogicSuccess(_ => controller.ping())

  override val endpoints: List[ServerEndpoint[Any, Future]] = List(pongEndpoint)
}
