package controllers

import monitoring.Metrics

import scala.concurrent.Future

class PingPongController {
  def ping(): Future[String] = {
    Metrics.endpointCallCounter.labelValues("ping", "success").inc()

    Future.successful("Pong")
  }
}
