package no.nav.pensjon.brev

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.config.mergeWith
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.pensjon.brev.template.brevbakerConfig

fun testBrevbakerAppTypst(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) =
    testBrevbakerApp(enableAllToggles = true, block = block)

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
                // else-en her blir aldri brukt, men må være her for å ikke gi oss kompileringsfeil
                "brevbaker.pdfByggerUrl" to if (isIntegrationTest) PDFByggerTestContainer.mappedUrl() else "denne blir ikke brukt",
            )
        )
    }
    val client = createClient {
        install(ContentNegotiation) {
            jackson { brevbakerConfig() }
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
    block(client)
}