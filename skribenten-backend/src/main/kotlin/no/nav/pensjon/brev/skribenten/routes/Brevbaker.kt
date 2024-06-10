package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*
import org.slf4j.LoggerFactory


data class RenderLetterRequest(val letterData: GenericBrevdata, val editedLetter: Edit.Letter?)
data class RenderLetterResponse(val editedLetter: Edit.Letter, val title: String, val sakspart: Sakspart, val signatur: Signatur)
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
        brevbakerService.renderLetter(call, brevkode, GeneriskRedigerbarBrevdata(EmptyBrevdata, request.letterData))
            .map { rendered ->
                call.respond(
                    RenderLetterResponse(
                        request.editedLetter?.updateEditedLetter(rendered) ?: rendered.toEdit(),
                        rendered.title,
                        rendered.sakspart,
                        rendered.signatur,
                    )
                )
            }.catch { message, status ->
                logger.error("Feil ved rendring av brevbaker brev Brevkode: $brevkode Melding: $message Status: $status")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved rendring av brevbaker brev.")
            }
    }
}

