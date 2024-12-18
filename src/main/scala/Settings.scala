import com.typesafe.config.ConfigFactory

object Settings {
  private val globalConfig = ConfigFactory.load

  object ServerConfig {
    private val config = globalConfig.getConfig("server")

    val host: String = config.getString("host")
    val port: Int = config.getInt("port")
  }

  object ClickHouseConfig {
    private val config = globalConfig.getConfig("clickhouse")

    val host: String = config.getString("host")
    val port: Int = config.getInt("port")
    val user: String = config.getString("user")
    val password: String = config.getString("password")
    val certificate: String = config.getString("certificate")
    val chProtocol: String = if (config.getBoolean("ssl")) "https" else "http"
  }

  object MonitoringConfig {
    private val config = globalConfig.getConfig("monitoring")

    val host: String = config.getString("host")
    val port: Int = config.getInt("port")
  }
}
