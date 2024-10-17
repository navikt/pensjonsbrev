package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.fasterxml.jackson.core.JacksonException
import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.Metrics.configureMetrics
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.requireAzureADConfig
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.tjenestebusJwt
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.SamhandlerClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.SamhandlerService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.setupServiceStatus
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.withCallId

fun Application.tjenestebussIntegrationApi(config: Config) {
    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
    }

    install(CallId) {
        header("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    install(ContentNegotiation) {
        jackson {
        }
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
        }
        // Work-around to print proper error message when call.receive<T> fails.
        exception<BadRequestException> { call, cause ->
            if (cause.cause is JacksonException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    cause.cause?.message ?: "Failed to deserialize json body: unknown reason"
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown failure")
            }
        }
    }
    val azureADConfig = config.requireAzureADConfig()

    install(Authentication) {
        tjenestebusJwt(azureADConfig)
    }


    configureMetrics()

    routing {
        val servicesConfig = config.getConfig("services")
        val samhandlerService =
            SamhandlerService(SamhandlerClientFactory(servicesConfig.getConfig("samhandlerService")))


        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        setupServiceStatus(
            samhandlerService,
        )

        authenticate(azureADConfig.name) {
            get("/ping") {
                call.respondText("Hello!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
            post("/hentSamhandlerAdresse") {
                val requestDto = call.receive<HentSamhandlerAdresseRequestDto>()
                call.respond(withCallId(samhandlerService) { hentSamhandlerPostadresse(requestDto.idTSSEkstern) })
            }
        }
    }
}

data class HentSamhandlerAdresseRequestDto(
    val idTSSEkstern: String,
)

