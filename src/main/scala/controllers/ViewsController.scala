package controllers

import dto.requests.CountViewsRequest
import dto.responses.CountViewsResponse
import services.ViewsService

import scala.concurrent.{ExecutionContext, Future}

class ViewsController(viewsService: ViewsService)(implicit executionContext: ExecutionContext) {
  def countViews(request: CountViewsRequest): Future[Either[String, CountViewsResponse]] = {

    viewsService
      .countViews(request.projectId, request.from, request.to)
      .map { count =>
        Right(CountViewsResponse(count))
      }
      .recover { case throwable: Throwable =>
        Left(throwable.getMessage)
      }
  }
}
