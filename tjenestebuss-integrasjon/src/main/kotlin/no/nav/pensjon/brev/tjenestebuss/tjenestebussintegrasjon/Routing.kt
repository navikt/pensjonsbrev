package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.typesafe.config.Config
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.ArkivTjenestebussService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSService
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.SamhandlerTjenestebussService
import java.util.*
import javax.xml.datatype.DatatypeFactory

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
        retrieveFromHeader("Nav-Call-Id")
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
        val arkivTjenestebussService = ArkivTjenestebussService(config.getConfig("services.tjenestebuss"), stsSercuritySOAPHandler)
        get("/testHentSamhandler") {
            try {
                val samhandler = samhandlerTjenestebussService.hentSamhandler()
                println(samhandler)
            } catch (e: Exception) {
                println(e)
            }
        }
        get("/testBestillbrev") {
            val gc = GregorianCalendar()
            gc.clear()
            gc.timeInMillis = 1699538037186L

            val xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc)
            try {
                val brev = arkivTjenestebussService.bestillBrev(ArkivTjenestebussService.BestillBrevDto(xgc))
                println(brev!!.journalpostId)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    //configureRouting(skribentenConfig)
}
