package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.model.LetterMetadata
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import org.slf4j.LoggerFactory

fun Route.brevmal(brevbakerService: BrevbakerService) {
    val logger = LoggerFactory.getLogger("brevbakerRoute")

    route("/brevmal/{brevkode}") {
        get {
            val brevkode = call.parameters.getBrevkode()
            val brevmal = brevbakerService.getRedigerbarTemplate(brevkode)?.let { LetterMetadata.Brevbaker(it) }
            if (brevmal != null) {
                call.respond(HttpStatusCode.OK, brevmal)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/modelSpecification") {
            val brevkode = call.parameters.getBrevkode()
            brevbakerService.getModelSpecification(brevkode)
                .onOk { call.respond(it) }
                .onError { message, status ->
                    logger.error("Feil ved henting av modelSpecification for ${brevkode.kode()}: Status:$status Melding: $message ")
                    call.respond(status, message)
                }
        }
    }
}

private fun Parameters.getBrevkode(): RedigerbarBrevkode =
    RedigerbarBrevkode(getOrFail("brevkode"))