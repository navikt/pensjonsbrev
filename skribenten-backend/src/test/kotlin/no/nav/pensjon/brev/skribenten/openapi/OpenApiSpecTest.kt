package no.nav.pensjon.brev.skribenten.openapi

import com.typesafe.config.ConfigFactory
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.skribentenApp
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Isolated
import org.testcontainers.postgresql.PostgreSQLContainer
import java.io.File

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

    private fun testConfig() = ConfigFactory.parseMap(
        buildMap {
            // Azure AD
            put("azureAD.issuer", "http://localhost:9999")
            put("azureAD.jwksUrl", "http://localhost:9999/jwks")
            put("azureAD.clientId", "fake-client-id")
            put("azureAD.clientSecret", "fake-secret")
            put("azureAD.tokenEndpoint", "http://localhost:9999/token")
            put("azureAD.preAuthApps", "[]")

            // CORS
            put("cors.host", "*")
            put("cors.schemes", listOf("http"))

            // Valkey (disabled)
            put("valkey.enabled", false)
            put("valkey.host", "fake")
            put("valkey.port", "0")
            put("valkey.username", "fake")
            put("valkey.password", "fake")

            // Kryptering
            put("krypteringsnoekkel", "UFsjvFmAas8j9GixQebcZpyKWCpBddD6")

            // Groups
            put("groups.pensjonUtland", "fake-group-id")
            put("groups.fortroligAdresse", "fake-group-id")
            put("groups.strengtFortroligAdresse", "fake-group-id")
            put("groups.pensjonSaksbehandler", "fake-group-id")
            put("groups.attestant", "fake-group-id")
            put("groups.veileder", "fake-group-id")
            put("groups.okonomi", "fake-group-id")
            put("groups.brukerhjelpA", "fake-group-id")
            put("groups.klagebehandler", "fake-group-id")

            // Services
            put("services.pen.url", "http://localhost:9999/pen/api/")
            put("services.pen.scope", "fake-scope")
            put("services.pdl.url", "http://localhost:9999/pdl")
            put("services.pdl.scope", "fake-scope")
            put("services.saf.url", "http://localhost:9999/saf")
            put("services.saf.rest_url", "http://localhost:9999/saf/rest")
            put("services.saf.scope", "fake-scope")
            put("services.pensjon_persondata.url", "http://localhost:9999")
            put("services.pensjon_persondata.scope", "fake-scope")
            put("services.pensjonRepresentasjon.url", "http://localhost:9999")
            put("services.pensjonRepresentasjon.scope", "fake-scope")
            put("services.krr.url", "http://localhost:9999")
            put("services.krr.scope", "fake-scope")
            put("services.brevbaker.url", "http://localhost:9999")
            put("services.brevbaker.scope", "fake-scope")
            put("services.brevmetadata.url", "http://localhost:9999")
            put("services.navansatt.url", "http://localhost:9999")
            put("services.navansatt.scope", "fake-scope")
            put("services.samhandlerProxy.url", "http://localhost:9999")
            put("services.samhandlerProxy.scope", "fake-scope")
            put("services.norg2.url", "http://localhost:9999/norg2/")
            put("services.externalApi.skribentenWebUrl", "http://localhost:9999")
            put("services.skjerming.url", "http://localhost:9999")
            put("services.skjerming.scope", "fake-scope")
            put("services.unleash.appName", "openapi-test")
            put("services.unleash.environment", "test")
            put("services.unleash.host", "http://localhost:9999")
            put("services.unleash.apiToken", "fake:token.fake")
            put("services.leader.url", null)
            put("services.foerstesidegenerator.url", "http://localhost:9999")
            put("services.foerstesidegenerator.scope", "fake-scope")

            // Dedicated database for OpenApiSpecTest (separate from SharedPostgres)
            put("services.database.host", postgres.host)
            put("services.database.port", postgres.getMappedPort(5432).toString())
            put("services.database.name", postgres.databaseName)
            put("services.database.username", postgres.username)
            put("services.database.password", postgres.password)
            put("services.database.maxPoolSize", 2)
        }
    )

    @Test
    fun `generates openapi spec from application routes`() = testApplication {
        application {
            skribentenApp(testConfig())
        }

        val specResponse = client.get("/swagger-internal/documentation.json")
        val specJson = specResponse.bodyAsText()
        assert(specJson.startsWith("{") || specJson.startsWith("[")) {
            "Expected JSON but got: ${specJson.take(500)}"
        }
        File("build/openapi-spec.json").writeText(specJson)
    }

}
