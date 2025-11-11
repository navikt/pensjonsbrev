package no.nav.pensjon.brev

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.template.brevbakerConfig

fun testBrevbakerApp(
    enableAllToggles: Boolean = false,
    isIntegrationTest: Boolean = true,
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
): Unit = testApplication {
    environment {
        val conf = if (isIntegrationTest) "application-integrationtests.conf" else "application.conf"
        config = ApplicationConfig(conf).mergeWith(
            MapApplicationConfig(
                "brevbaker.unleash.fakeUnleashEnableAll" to "$enableAllToggles",
            )
        )
    }
    val client = createClient {
        install(ContentNegotiation) {
            jackson { brevbakerConfig() }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
    block(client)
}