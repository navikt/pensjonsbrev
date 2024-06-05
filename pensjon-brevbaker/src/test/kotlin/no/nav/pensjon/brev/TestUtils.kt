package no.nav.pensjon.brev

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.template.brevbakerConfig

fun testBrevbakerApp(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit): Unit = testApplication {
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