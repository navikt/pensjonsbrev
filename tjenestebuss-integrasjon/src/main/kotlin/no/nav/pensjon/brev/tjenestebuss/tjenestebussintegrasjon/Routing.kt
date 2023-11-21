package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.SamhandlerTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivClient
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Failure.FailureType.MANGLER_OBLIGATORISK_INPUT
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Journalpost
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.SamhandlerTypeCode

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

    routing {
        val stsService = STSService(config.getConfig("services.sts"))
        val stsSercuritySOAPHandler = STSSercuritySOAPHandler(stsService)
        val samhandlerTjenestebussService =
            SamhandlerTjenestebussService(config.getConfig("services.tjenestebuss"), stsSercuritySOAPHandler)

        val arkivClient = ArkivClient(config.getConfig("services.tjenestebuss"), stsSercuritySOAPHandler).arkivClient()
        val arkivTjenestebussService = ArkivTjenestebussService(arkivClient)

        post("/hentSamhandler") {
            val requestDto = call.receive<HentSamhandlerRequestDto>()
            val samhandlerResponse = samhandlerTjenestebussService.hentSamhandler(requestDto)
            if(samhandlerResponse is HentSamhandlerResponseDto.Samhandler){
                call.respond(HttpStatusCode.OK, samhandlerResponse)
            } else if((samhandlerResponse as HentSamhandlerResponseDto.Failure).failureType == HentSamhandlerResponseDto.Failure.FailureType.IKKE_FUNNET){
                call.respond(HttpStatusCode.NotFound, samhandlerResponse)
            } else {
                call.respond(HttpStatusCode.BadRequest, samhandlerResponse)
            }
        }
        post("/finnSamhandler") {
            val requestDto = call.receive<FinnSamhandlerRequestDto>()
            val samhandlerResponse = samhandlerTjenestebussService.finnSamhandler(requestDto)
            if(samhandlerResponse is FinnSamhandlerResponseDto.Success){
                call.respond(HttpStatusCode.OK, samhandlerResponse)
            } else {
                call.respond(HttpStatusCode.InternalServerError, samhandlerResponse)
            }
        }
        post("/bestillbrev") {
            val requestDto = call.receive<BestillBrevRequestDto>()
                val arkivResponse = arkivTjenestebussService.bestillBrev(requestDto)
            if(arkivResponse is Journalpost)
                call.respond(HttpStatusCode.OK, arkivResponse)
            else if ((arkivResponse as BestillBrevResponseDto.Failure).failureType == MANGLER_OBLIGATORISK_INPUT){
                call.respond(HttpStatusCode.BadRequest, arkivResponse)
            } else {
                call.respond(HttpStatusCode.InternalServerError, arkivResponse)
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

