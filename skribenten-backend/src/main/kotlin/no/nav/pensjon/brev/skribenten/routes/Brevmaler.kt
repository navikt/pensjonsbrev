package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.LetterMetadata

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService
) {
    get("/lettertemplates/e-blanketter") {
        val hasAccess = call.principal<UserPrincipal>()?.isPensjonUtlandGroup() ?: false
        if (hasAccess) call.respond(brevmetadataService.getEblanketter()) else emptyList<LetterMetadata>()
    }
    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail("sakType")
        call.respond(
            brevmetadataService.getRedigerbareBrev(sakType),
        )
    }
}