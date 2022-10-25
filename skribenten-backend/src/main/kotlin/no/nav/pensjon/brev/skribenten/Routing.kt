package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.*

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureAdService(authConfig)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)

    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(authConfig.name) {
            post("/test") {
                val sak = penService.hentSak(call, 22958874)
                val dto = call.receive<Testing>()
                call.application.log.info("mottok: $dto")
                call.respond(dto.copy(name = "hei ${dto.name}, sak 22958874 hentet"))
            }
        }
    }
}

data class Testing(val name: String, val age: Int)