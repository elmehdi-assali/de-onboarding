package services

import com.contentsquare.clickhousedriver.clickhouse.client.ClickHouseClient
import com.contentsquare.clickhousedriver.clickhousequery.driver.CHTables.CHTable
import com.contentsquare.clickhousedriver.clickhousequery.driver.expr.methods.AggregatedFunction.sum
import com.contentsquare.clickhousedriver.clickhousequery.driver.expr.methods.ExprImplicitMethod.{
  BooleanExprMethods,
  DateTimeExprMethods,
  IntExprMethods
}
import com.contentsquare.clickhousedriver.clickhousequery.driver.expr.{Column, NamedExpr}
import com.contentsquare.clickhousedriver.clickhousequery.driver.query.CHSetting.DistributedGroupByNoMerge
import com.contentsquare.clickhousedriver.clickhousequery.driver.query.QueryPrinterSettings
import com.contentsquare.clickhousedriver.clickhousequery.driver.query.SelectQuery.select

import java.time.ZonedDateTime
import scala.concurrent.{ExecutionContext, Future}

class ViewsService()(
    implicit clickHouseClient: ClickHouseClient,
    queryPrinterSettings: QueryPrinterSettings,
    executionContext: ExecutionContext
) {
  private object SessionsTable extends CHTable {
    override def _database: String = "default"

    override def _name: String = "sessions"

    val projectIdColumn: Column[Int] = Column[Int]("project_id")
    val sessionTimeColumn: Column[ZonedDateTime] = Column[ZonedDateTime]("session_time")
    val numberOfViewsColumn: Column[Int] = Column[Int]("session_number_of_views")
    val signColumn: Column[Int] = Column[Int]("sign")
    val sumOfViewsColumn: NamedExpr[BigInt] = sum(numberOfViewsColumn * signColumn) as "sumViewsColumn"
  }

  def countViews(projectId: Int, from: ZonedDateTime, to: ZonedDateTime): Future[Int] = {
    val query = select(SessionsTable.sumOfViewsColumn)
      .from(SessionsTable)
      .where(
        SessionsTable.projectIdColumn === projectId
          AND
          SessionsTable.sessionTimeColumn >= from
          AND
          SessionsTable.sessionTimeColumn <= to
      )
      .settings(DistributedGroupByNoMerge(0))

    query.execute.map { response =>
      response.data.map(row => row.getInt(SessionsTable.sumOfViewsColumn.asSQLString)).sum
    }
  }
}
