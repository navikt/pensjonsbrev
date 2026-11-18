package no.nav.pensjon.brev.brevoppskrift.lagring

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.serialization.Serializable
import org.flywaydb.core.Flyway

@Serializable
data class DatabaseConfig(
    val jdbcUrl: String? = null,
    val username: String,
    val password: String,
    val host: String? = null,
    val port: Int = 5432,
    val name: String? = null,
    val maxPoolSize: Int = 3,
) {
    init {
        require(jdbcUrl?.isNotBlank() ?: (!host.isNullOrBlank() && !name.isNullOrBlank())) {
            "Database requires jdbcUrl or host and name"
        }
        require(username.isNotBlank() && password.isNotBlank()) { "Database credentials must not be blank" }
        require(port in 1..65535 && maxPoolSize > 0) { "Invalid database port or pool size" }
    }
}

fun openDatabase(config: DatabaseConfig, metrics: MeterRegistry? = null): HikariDataSource {
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
