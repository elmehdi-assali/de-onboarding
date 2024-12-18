package routes

import Codecs._
import controllers.ViewsController
import dto.requests.CountViewsRequest
import dto.responses.CountViewsResponse
import sttp.tapir.server.ServerEndpoint
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._

import scala.concurrent.Future

class ViewsRoute(controller: ViewsController) extends Route {
  private val countViewsEndpoint = endpoint
    .in("views")
    .in(jsonBody[CountViewsRequest])
    .out(jsonBody[CountViewsResponse])
    .errorOut(stringBody)
    .serverLogic(input => controller.countViews(input))

  override val endpoints: List[ServerEndpoint[Any, Future]] = List(countViewsEndpoint)
}
