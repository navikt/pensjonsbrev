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
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.Metrics.configureMetrics
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.requireAzureADConfig
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.tjenestebusJwt
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevExtreamRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Failure.FailureType.MANGLER_OBLIGATORISK_INPUT
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Success
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.DokumentproduksjonService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.RedigerDoksysDokumentResponseDto.Failure.FailureType.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.extreambrev.ExtreamBrevService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.extreambrev.RedigerExtreamDokumentResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.SamhandlerTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto.Failure.FailureType
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.SamhandlerTypeCode
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSService
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
    val azureADConfig = config.requireAzureADConfig()

    install(Authentication) {
        tjenestebusJwt(azureADConfig)
    }


    configureMetrics()

    routing {
        val stsService = STSService(config.getConfig("services.sts"))
        val stsSercuritySOAPHandler = STSSercuritySOAPHandler(stsService)
        val servicesConfig = config.getConfig("services")
        val samhandlerTjenestebussService = SamhandlerTjenestebussService(servicesConfig.getConfig("tjenestebuss"), stsSercuritySOAPHandler)
        val arkivTjenestebussService = ArkivTjenestebussService(servicesConfig.getConfig("tjenestebuss"), stsSercuritySOAPHandler)
        val dokumentProduksjonService = DokumentproduksjonService(servicesConfig.getConfig("dokprod"), stsSercuritySOAPHandler)
        val extreamBrevService = ExtreamBrevService(servicesConfig, stsSercuritySOAPHandler)


        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(azureADConfig.name) {

            post("/hentSamhandler") {
                val requestDto = call.receive<HentSamhandlerRequestDto>()

                val samhandlerResponse = withCallId(samhandlerTjenestebussService) { hentSamhandler(requestDto) }

                when (samhandlerResponse) {
                    is HentSamhandlerResponseDto.Failure -> {
                        if (samhandlerResponse.failureType == FailureType.IKKE_FUNNET) {
                            call.respond(HttpStatusCode.NotFound, samhandlerResponse)
                        } else {
                            call.respond(HttpStatusCode.BadRequest, samhandlerResponse)
                        }
                    }

                    is HentSamhandlerResponseDto.Success -> call.respond(HttpStatusCode.OK, samhandlerResponse)
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
            post("/bestillExtreamBrev") {
                val requestDto = call.receive<BestillBrevExtreamRequestDto>()

                when (val arkivResponse = withCallId(arkivTjenestebussService) { bestillBrev(requestDto) }) {
                    is Success -> call.respond(HttpStatusCode.OK, arkivResponse)
                    is BestillBrevResponseDto.Failure -> {
                        if (arkivResponse.failureType == MANGLER_OBLIGATORISK_INPUT) {
                            call.respond(HttpStatusCode.BadRequest, arkivResponse)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError, arkivResponse)
                        }
                    }
                }
            }
            post("/redigerDoksysBrev") {
                val requestDto = call.receive<RedigerDoksysDokumentRequestDto>()
                when (val dokumentResponse = withCallId(dokumentProduksjonService) { redigerDokument(requestDto) }) {
                    is RedigerDoksysDokumentResponseDto.Success -> call.respond(HttpStatusCode.OK, dokumentResponse)
                    is RedigerDoksysDokumentResponseDto.Failure -> {
                        call.respond(
                            when (dokumentResponse.failureType) {
                                LASING -> HttpStatusCode.InternalServerError
                                IKKE_TILLATT -> HttpStatusCode.Forbidden
                                VALIDERING_FEILET -> HttpStatusCode.BadRequest
                                IKKE_FUNNET -> HttpStatusCode.NotFound
                                IKKE_TILGANG -> HttpStatusCode.Unauthorized
                                LUKKET -> HttpStatusCode.Locked
                            }, dokumentResponse
                        )
                    }
                }
            }
            post("/redigerExtreamBrev") {
                val requestDto = call.receive<RedigerExtreamDokumentRequestDto>()
                when (val response = withCallId(extreamBrevService) { hentExtreamBrevUrl(requestDto) }) {
                    is RedigerExtreamDokumentResponseDto.Success -> call.respond(HttpStatusCode.OK, response)
                    is RedigerExtreamDokumentResponseDto.Failure -> call.respond((HttpStatusCode.InternalServerError), response)
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

class RedigerDoksysDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

class RedigerExtreamDokumentRequestDto(
    val dokumentId: String,
)