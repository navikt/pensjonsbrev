package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.services.*
import java.util.Base64

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)
    val brevbakerService = BrevbakerService(skribentenConfig.getConfig("services.brevbaker"), authService)

    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(authConfig.name) {
            post("/test/pen") {
                val sak = penService.hentSak(call, 22958874)
                respondWithResult(sak)
            }

            get("/test/brevbaker") {
                val brev = brevbakerService.genererBrev(call)
                respondWithResult(brev, onOk = { respondBytes(Base64.getDecoder().decode(it.base64pdf), ContentType.Application.Pdf) })
            }
        }
    }
}

data class Testing(val name: String, val age: Int)