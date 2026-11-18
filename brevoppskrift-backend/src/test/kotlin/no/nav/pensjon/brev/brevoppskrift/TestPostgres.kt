package no.nav.pensjon.brev.brevoppskrift

import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseConfig
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.postgresql.PostgreSQLContainer
import java.sql.DriverManager
import java.util.UUID

class TestPostgres : BeforeAllCallback, AfterAllCallback {
    private val postgres = standalone()

    override fun beforeAll(context: ExtensionContext) { postgres.start() }
    override fun afterAll(context: ExtensionContext) { postgres.stop() }

    fun database(): DatabaseConfig {
        val name = "test_${UUID.randomUUID().toString().replace("-", "")}"
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password).use { connection ->
            connection.createStatement().use { it.execute("CREATE DATABASE $name") }
        }
        return DatabaseConfig("jdbc:postgresql://${postgres.host}:${postgres.getMappedPort(5432)}/$name", postgres.username, postgres.password)
    }

    companion object {
        fun standalone(): PostgreSQLContainer = PostgreSQLContainer("postgres:18-alpine")
    }
}
