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
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivClient
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.ArkivTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto

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

        get("/testHentSamhandler") {
            try {
                val samhandler = samhandlerTjenestebussService.hentSamhandler()
                println(samhandler)
            } catch (e: Exception) {
                println(e)
            }
        }
        post("/bestillbrev") {
            val requestDto = call.receive<BestillBrevRequestDto>()
                val arkivResponse = arkivTjenestebussService.bestillBrev(requestDto)
            if(arkivResponse is BestillBrevResponseDto.Journalpost)
                call.respond(HttpStatusCode.OK, arkivResponse)
            else {
                call.respond(HttpStatusCode.InternalServerError, arkivResponse)
            }
        }
    }

    //configureRouting(skribentenConfig)
}
