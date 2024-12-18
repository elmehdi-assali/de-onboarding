import Settings.{ClickHouseConfig, MonitoringConfig, ServerConfig}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import com.contentsquare.clickhousedriver.clickhouse.ClickHouseSettings
import com.contentsquare.clickhousedriver.clickhouse.client.ClickHouseClient
import com.contentsquare.clickhousedriver.clickhouse.client.http.ClickHouseHttpClient
import com.contentsquare.clickhousedriver.clickhousequery.driver.query.QueryPrinterSettings
import com.typesafe.scalalogging.StrictLogging
import controllers._
import io.prometheus.metrics.exporter.httpserver.HTTPServer
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics
import routes._
import services.ViewsService
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.ExecutionContextExecutor

object App extends App with StrictLogging {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val clickHouseClient: ClickHouseClient = new ClickHouseHttpClient(
    ClickHouseSettings(
      s"${ClickHouseConfig.chProtocol}://${ClickHouseConfig.host}:${ClickHouseConfig.port}",
      BasicHttpCredentials(ClickHouseConfig.user, ClickHouseConfig.password),
      Option(ClickHouseConfig.certificate)
    )
  )
  implicit val queryPrinterSettings: QueryPrinterSettings = QueryPrinterSettings(2, prettyPrint = true, 0)

  val routes: List[Route] = List(
    new PingPongRoute(new PingPongController),
    new ViewsRoute(new ViewsController(new ViewsService()))
  )
  Http()
    .newServerAt(ServerConfig.host, ServerConfig.port)
    .bind(AkkaHttpServerInterpreter().toRoute(routes.flatMap(route => route.endpoints)))
    .map { serverBinding =>
      logger.info("API server running on {}", serverBinding.localAddress)
    }

  JvmMetrics.builder().register()
  val server = HTTPServer
    .builder()
    .port(MonitoringConfig.port)
    .buildAndStart()
  logger.info("Monitoring server running on 0.0.0.0:{}", server.getPort)
}
