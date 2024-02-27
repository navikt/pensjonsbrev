package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PenService

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService,
) {
    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail<PenService.SakType>("sakType")
        val isVedtaksKontekst = call.request.queryParameters["includeVedtak"] == "true"

        val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)

        val redigerbareBrev = brevmetadataService.getRedigerbareBrev(sakType, isVedtaksKontekst)
        val eblanketter = if (hasAccessToEblanketter) brevmetadataService.getEblanketter() else emptyList()

        call.respond(redigerbareBrev + eblanketter)
    }
}