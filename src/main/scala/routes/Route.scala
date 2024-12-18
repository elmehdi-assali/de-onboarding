package routes

import sttp.tapir.server.ServerEndpoint

import scala.concurrent.Future

trait Route {
  val endpoints: List[ServerEndpoint[Any, Future]]
}
