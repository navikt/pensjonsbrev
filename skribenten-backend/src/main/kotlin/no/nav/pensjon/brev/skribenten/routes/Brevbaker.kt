package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import no.nav.pensjon.brev.skribenten.updatedEditedLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter


data class RenderLetterRequest(val letterData: GenericBrevdata, val editedLetter: EditedJsonLetter?)
data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)
class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

// TODO: Flytt til topp-rute /brevbaker
fun Route.brevbakerRoute(brevbakerService: BrevbakerService) {
    get("/template/{brevkode}") {
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")

        when (val template = brevbakerService.getTemplate(call, brevkode)) {
            is ServiceResult.AuthorizationError -> TODO()
            is ServiceResult.Error -> TODO()
            is ServiceResult.Ok -> call.respondText(template.result, ContentType.Application.Json)
        }
    }

    post<RenderLetterRequest>("/letter/{brevkode}") { request ->
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")

        when (val rendered = brevbakerService.renderLetter(call, brevkode, request.letterData)) {
            is ServiceResult.AuthorizationError -> TODO()
            is ServiceResult.Error -> TODO()
            is ServiceResult.Ok -> call.respond(
                request.editedLetter?.let { updatedEditedLetter(it, rendered.result) }
                    ?: rendered.result
            )
        }
    }
}
