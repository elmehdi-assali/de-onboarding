package controllers

import com.typesafe.scalalogging.StrictLogging
import dto.requests.CountViewsRequest
import dto.responses.CountViewsResponse
import services.ViewsService
import monitoring.Metrics

import scala.concurrent.{ExecutionContext, Future}

class ViewsController(viewsService: ViewsService)(implicit executionContext: ExecutionContext) extends StrictLogging {
  def countViews(request: CountViewsRequest): Future[Either[String, CountViewsResponse]] = {
    viewsService
      .countViews(request.projectId, request.from, request.to)
      .map { count =>
        Metrics.endpointCallCounter.labelValues("views", "success").inc(1)
        Right(CountViewsResponse(count))
      }
      .recover { case throwable: Throwable =>
        Metrics.endpointCallCounter.labelValues("views", "failure").inc(1)
        logger.error("Failed to count views", throwable)
        Left(throwable.getMessage)
      }
  }
}
