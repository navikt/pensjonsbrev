package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*

fun testApplication(block: suspend ApplicationTestBuilder.() -> Unit): Unit =
    io.ktor.server.testing.testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        application { tjenestebussIntegrationApi(ConfigFactory.load("application-test.conf").getConfig("tjenestebussintegrasjon")) }
        block()
    }

fun createTestApplication(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit): Unit =
    io.ktor.server.testing.testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            install(ContentNegotiation) {
                jackson { }
            }
        }
        val client =
            createClient {
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                    jackson { }
                }
            }
        block(client)
    }
