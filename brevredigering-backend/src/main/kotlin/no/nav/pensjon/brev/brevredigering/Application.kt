package no.nav.pensjon.brev.brevredigering

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import no.nav.pensjon.brev.brevredigering.Metrics.configureMetrics

fun main() {
    embeddedServer(Netty, port = 8082, host = "0.0.0.0") {
        configureRouting()

        install(CallLogging) {
            callIdMdc("x_correlationId")
            disableDefaultColors()
            val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
            filter {
                !ignorePaths.contains(it.request.path())
            }
        }

        install(StatusPages) {
            exception<JacksonException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
            }
            // Work-around to print proper error message when call.receive<T> fails.
            exception<BadRequestException> { call, cause ->
                if (cause.cause is JacksonException) {
                    call.respond(HttpStatusCode.BadRequest, cause.cause?.message ?: "Failed to deserialize json body: unknown reason")
                } else {
                    call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown failure")
                }
            }
            exception<ParameterConversionException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.message?: "Failed to convert path parameter to required type: unknown cause")
            }
        }

        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }

        configureMetrics()
    }.start(wait = true)
}
