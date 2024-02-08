package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService
) {
    get("/lettertemplates/e-blanketter") {
        //TODO figure out who has access to e-blanketter and filter them out. then only display eblanketter when you get the metadata back.
        call.respond(brevmetadataService.getEblanketter())
    }
    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail("sakType")
        val includeVedtak = call.request.queryParameters["includeVedtak"] == "true"
        call.respond(
            brevmetadataService.getRedigerbareBrev(sakType, includeVedtak),
        )
    }
}