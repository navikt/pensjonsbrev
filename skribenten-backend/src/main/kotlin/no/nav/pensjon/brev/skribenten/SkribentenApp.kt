package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import no.nav.pensjon.brev.skribenten.Metrics.configureMetrics
import no.nav.pensjon.brev.skribenten.auth.*


fun main() {
    val skribentenConfig: Config = ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
        .resolveWith(ConfigFactory.load("azuread")) // loads azuread secrets for local
        .getConfig("skribenten")

    embeddedServer(Netty, port = skribentenConfig.getInt("port"), host = "0.0.0.0") {
        install(CallLogging) {
            callIdMdc("x_correlationId")
            disableDefaultColors()
            val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
            filter {
                !ignorePaths.contains(it.request.path())
            }
        }

        install(CallId) {
            retrieveFromHeader("Nav-Call-Id")
            generate()
            verify { it.isNotEmpty() }
        }

        install(StatusPages) {
            exception<JacksonException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
            }
        }

        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }

        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Patch)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)
            allowHost("localhost:3000", schemes = listOf("http"))
            // TODO: Legg til allowHost for dev og prod med https
        }

        val azureADConfig = skribentenConfig.requireAzureADConfig()
        install(Authentication) {
            skribentenJwt(azureADConfig)
        }

        configureRouting(azureADConfig, skribentenConfig)
        configureMetrics()
    }.start(wait = true)
}
