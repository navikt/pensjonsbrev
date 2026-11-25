package no.nav.pensjon.brev.brevoppskrift.lagring

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.serialization.Serializable
import org.flywaydb.core.Flyway

interface DatabaseSettings {
    val jdbcUrl: String?
    val username: String
    val password: String
    val host: String?
    val port: Int
    val name: String?
    val maxPoolSize: Int
}

@Serializable
data class DatabaseConfig(
    override val jdbcUrl: String? = null,
    override val username: String,
    override val password: String,
    override val host: String? = null,
    override val port: Int = 5432,
    override val name: String? = null,
    override val maxPoolSize: Int = 3,
) : DatabaseSettings {
    init {
        require(jdbcUrl?.isNotBlank() ?: (!host.isNullOrBlank() && !name.isNullOrBlank())) {
            "Database requires jdbcUrl or host and name"
        }
        require(username.isNotBlank() && password.isNotBlank()) { "Database credentials must not be blank" }
        require(port in 1..65535 && maxPoolSize > 0) { "Invalid database port or pool size" }
    }
}

fun openDatabase(config: DatabaseSettings, metrics: MeterRegistry? = null): HikariDataSource {
    val dataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = config.jdbcUrl ?: "jdbc:postgresql://${config.host}:${config.port}/${config.name}"
            username = config.username
            password = config.password
            maximumPoolSize = config.maxPoolSize
            minimumIdle = 1
            connectionTimeout = 5000
            validationTimeout = 3000
            maxLifetime = 600_000
            keepaliveTime = 120_000
            poolName = "brevoppskrift"
            if (metrics != null) metricsTrackerFactory = MicrometerMetricsTrackerFactory(metrics)
            initializationFailTimeout = 10_000
        },
    )
    try {
        Flyway.configure().dataSource(dataSource).validateMigrationNaming(true).load().migrate()
        return dataSource
    } catch (exception: Exception) {
        dataSource.close()
        throw exception
    }
}
