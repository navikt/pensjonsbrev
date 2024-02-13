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
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevExstreamRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.DokumentproduksjonService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.exstreambrev.RedigerExstreamBrevService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.PsakSamhandlerTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.SamhandlerService
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
        val stsService = STSService(config.getConfig("services.sts"))
        val stsSercuritySOAPHandler = STSSercuritySOAPHandler(stsService)
        val servicesConfig = config.getConfig("services")
        val psakSamhandlerTjenestebussService =
            PsakSamhandlerTjenestebussService(servicesConfig.getConfig("tjenestebuss"), stsSercuritySOAPHandler)
        val samhandlerService = SamhandlerService(servicesConfig.getConfig("samhandlerService"))
        val arkivTjenestebussService =
            ArkivTjenestebussService(servicesConfig.getConfig("tjenestebuss"), stsSercuritySOAPHandler)
        val dokumentProduksjonService =
            DokumentproduksjonService(servicesConfig.getConfig("dokprod"), stsSercuritySOAPHandler)
        val redigerExstreamBrevService = RedigerExstreamBrevService(servicesConfig, stsSercuritySOAPHandler)


        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(azureADConfig.name) {
            get("/ping") {
                call.respondText("Hello!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
            post("/hentSamhandler") {
                val requestDto = call.receive<HentSamhandlerRequestDto>()
                call.respond(withCallId(psakSamhandlerTjenestebussService) { hentSamhandler(requestDto) })
            }
            post("/hentSamhandlerAdresse"){
                val requestDto = call.receive<HentSamhandlerAdresseRequestDto>()
                call.respond(withCallId(samhandlerService) { hentSamhandlerPostadresse(requestDto.idTSSEkstern) })
            }
            post("/finnSamhandler") {
                val requestDto = call.receive<FinnSamhandlerRequestDto>()
                call.respond(withCallId(psakSamhandlerTjenestebussService) { finnSamhandler(requestDto) })
            }
            post("/bestillExstreamBrev") {
                val requestDto = call.receive<BestillBrevExstreamRequestDto>()
                call.respond(withCallId(arkivTjenestebussService) { bestillBrev(requestDto) })
            }
            post("/redigerExstreamBrev") {
                val requestDto = call.receive<RedigerExstreamDokumentRequestDto>()
                call.respond(withCallId(redigerExstreamBrevService) { hentExstreamBrevUrl(requestDto) })
            }
            post("/redigerDoksysBrev") {
                val requestDto = call.receive<RedigerDoksysDokumentRequestDto>()
                call.respond(withCallId(dokumentProduksjonService) { redigerDokument(requestDto) })
            }
        }
    }
}

data class HentSamhandlerRequestDto(
    val idTSSEkstern: String,
    val hentDetaljert: Boolean,
)

data class HentSamhandlerAdresseRequestDto(
    val idTSSEkstern: String,
)

data class FinnSamhandlerRequestDto(
    val navn: String,
    val samhandlerType: SamhandlerTypeCode,
)

data class RedigerDoksysDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

data class RedigerExstreamDokumentRequestDto(
    val journalpostId: String,
)