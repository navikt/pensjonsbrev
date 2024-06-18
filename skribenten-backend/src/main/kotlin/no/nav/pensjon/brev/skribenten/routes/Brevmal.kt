package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import org.slf4j.LoggerFactory

fun Route.brevmal(brevbakerService: BrevbakerService) {
    val logger = LoggerFactory.getLogger("brevbakerRoute")

    get("/brevmal/{brevkode}/modelSpecification") {
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")
        brevbakerService.getModelSpecification(call, brevkode)
            .onOk { call.respondText(it, ContentType.Application.Json) }
            .onError { message, status ->
                logger.error("Feil ved henting av modelSpecification for ${brevkode.name}: Status:$status Melding: $message ")
                call.respond(status, message)
            }
    }

}

