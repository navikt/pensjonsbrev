package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.fasterxml.jackson.core.JacksonException
import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.JwtConfig
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Failure.FailureType.MANGLER_OBLIGATORISK_INPUT
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Journalpost
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.DokumentproduksjonService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.dto.RedigerDokumentResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.SamhandlerTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.SamhandlerTypeCode
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.withCallId

fun Application.tjenestebussIntegrationApi(authConfig: JwtConfig, config: Config) {
    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
    }

    install(CallId) {
        retrieveFromHeader("X-Request-ID")
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

    routing {
        val stsService = STSService(config.getConfig("services.sts"))
        val stsSercuritySOAPHandler = STSSercuritySOAPHandler(stsService)

        val samhandlerTjenestebussService =
            SamhandlerTjenestebussService(config.getConfig("services.tjenestebuss"), stsSercuritySOAPHandler)

        val arkivTjenestebussService =
            ArkivTjenestebussService(config.getConfig("services.tjenestebuss"), stsSercuritySOAPHandler)

        val dokumentProduksjonService =
            DokumentproduksjonService(config.getConfig("services.dokprod"), stsSercuritySOAPHandler)


        authenticate(authConfig.name) {

            post("/hentSamhandler") {
                val requestDto = call.receive<HentSamhandlerRequestDto>()

                val samhandlerResponse = withCallId(samhandlerTjenestebussService) { hentSamhandler(requestDto) }

                when (samhandlerResponse) {
                    is HentSamhandlerResponseDto.Failure -> {
                        if (samhandlerResponse.failureType == HentSamhandlerResponseDto.Failure.FailureType.IKKE_FUNNET) {
                            call.respond(HttpStatusCode.NotFound, samhandlerResponse)
                        } else {
                            call.respond(HttpStatusCode.BadRequest, samhandlerResponse)
                        }
                    }

                    is HentSamhandlerResponseDto.Samhandler -> call.respond(HttpStatusCode.OK, samhandlerResponse)
                }
            }
            post("/finnSamhandler") {
                val requestDto = call.receive<FinnSamhandlerRequestDto>()

                val samhandlerResponse = withCallId(samhandlerTjenestebussService) {
                    finnSamhandler(requestDto)
                }
                when (samhandlerResponse) {
                    is FinnSamhandlerResponseDto.Failure -> call.respond(
                        HttpStatusCode.InternalServerError,
                        samhandlerResponse
                    )

                    is FinnSamhandlerResponseDto.Success -> call.respond(HttpStatusCode.OK, samhandlerResponse)
                }
            }
            post("/bestillbrev") {
                val requestDto = call.receive<BestillBrevRequestDto>()

                when (val arkivResponse = withCallId(arkivTjenestebussService) { bestillBrev(requestDto) }) {
                    is Journalpost -> call.respond(HttpStatusCode.OK, arkivResponse)
                    is BestillBrevResponseDto.Failure -> {
                        if (arkivResponse.failureType == MANGLER_OBLIGATORISK_INPUT) {
                            call.respond(HttpStatusCode.BadRequest, arkivResponse)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError, arkivResponse)
                        }
                    }
                }
            }
        }

        post("/bestillbrev") {
            val requestDto = call.receive<RedigerDokumentRequestDto>()

            when (val dokumentResponse = withCallId(dokumentProduksjonService) { hentSamhandler(requestDto) }) {
                is RedigerDokumentResponseDto.Dokument -> call.respond(HttpStatusCode.OK, dokumentResponse)
                is RedigerDokumentResponseDto.Failure -> {
                    when(dokumentResponse.failureType){
                        RedigerDokumentResponseDto.Failure.FailureType.LASING -> call.respond(HttpStatusCode.InternalServerError, dokumentResponse)
                        RedigerDokumentResponseDto.Failure.FailureType.IKKE_TILLATT -> call.respond(HttpStatusCode.Forbidden, dokumentResponse)
                        RedigerDokumentResponseDto.Failure.FailureType.VALIDERING_FEILET -> call.respond(HttpStatusCode.BadRequest, dokumentResponse)
                        RedigerDokumentResponseDto.Failure.FailureType.IKKE_FUNNET -> call.respond(HttpStatusCode.NotFound, dokumentResponse)
                        RedigerDokumentResponseDto.Failure.FailureType.IKKE_TILGANG -> call.respond(HttpStatusCode.Unauthorized, dokumentResponse)
                        RedigerDokumentResponseDto.Failure.FailureType.LUKKET -> call.respond(HttpStatusCode.Locked, dokumentResponse)
                    }
                }
            }
        }
    }
}

class HentSamhandlerRequestDto(
    val idTSSEkstern: String,
    val hentDetaljert: Boolean,
)

class FinnSamhandlerRequestDto(
    val navn: String,
    val samhandlerType: SamhandlerTypeCode,
)

class RedigerDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)
