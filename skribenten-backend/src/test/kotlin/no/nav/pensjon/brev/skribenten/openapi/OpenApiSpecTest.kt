package no.nav.pensjon.brev.skribenten.openapi

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.config.mergeWith
import io.ktor.server.testing.*
import no.nav.pensjon.brev.skribenten.initADGroups
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Isolated
import org.testcontainers.postgresql.PostgreSQLContainer
import java.io.File

@Tag("openapi-spec")
@Isolated
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenApiSpecTest {

    private lateinit var postgres: PostgreSQLContainer

    @BeforeAll
    fun setup() {
        postgres = PostgreSQLContainer("postgres:17-alpine").also { it.start() }
        initADGroups()
    }

    @AfterAll
    fun teardown() {
        postgres.stop()
    }

    fun databaseConfig() = MapApplicationConfig(
        "skribenten.database.host" to postgres.host,
        "skribenten.database.port" to postgres.getMappedPort(5432).toString(),
        "skribenten.database.name" to postgres.databaseName,
        "skribenten.database.username" to postgres.username,
        "skribenten.database.password" to postgres.password,
        "skribenten.database.maxPoolSize" to "2",
    )

    @Test
    fun `generates openapi spec from application routes`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
                .mergeWith(databaseConfig())
        }

        val specResponse = client.get("/swagger-internal/documentation.json")
        val specJson = specResponse.bodyAsText()
        assert(specJson.startsWith("{") || specJson.startsWith("[")) {
            "Expected JSON but got: ${specJson.take(500)}"
        }
        File("build/openapi-spec.json").writeText(specJson)
    }

}
