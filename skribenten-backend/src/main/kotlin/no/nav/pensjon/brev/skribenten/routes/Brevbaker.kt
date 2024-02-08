package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.updatedEditedLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import org.slf4j.LoggerFactory


data class RenderLetterRequest(val letterData: GenericBrevdata, val editedLetter: EditedJsonLetter?)
data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)
class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

// TODO: Flytt til topp-rute /brevbaker
fun Route.brevbakerRoute(brevbakerService: BrevbakerService) {
    val logger = LoggerFactory.getLogger("brevbakerRoute")

    get("/template/{brevkode}") {
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")
        brevbakerService.getTemplate(call, brevkode)
            .map { call.respondText(it, ContentType.Application.Json) }
            .catch { message, status ->
                logger.error("Feil ved henting av brevkode: Status:$status Melding: $message ")
                call.respond(status, message)
            }
    }

    post<RenderLetterRequest>("/letter/{brevkode}") { request ->
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")
        brevbakerService.renderLetter(call, brevkode, request.letterData)
            .map { rendered ->
                call.respond(request.editedLetter?.let { updatedEditedLetter(it, rendered) } ?: rendered)
            }.catch { message, status ->
                logger.error("Feil ved rendring av brevbaker brev Brevkode: $brevkode Melding: $message Status: $status")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved rendring av brevbaker brev.")
            }
    }
}

