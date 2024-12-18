package controllers

import scala.concurrent.Future

class PingPongController {
  def ping(): Future[String] = Future.successful("Pong")
}
