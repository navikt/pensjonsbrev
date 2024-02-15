package no.nav.pensjon.brev.skribenten.routes

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.isInGroup
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PenService

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService,
    groupsConfig: Config
) {
    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail<PenService.SakType>("sakType")
        val includeVedtak = call.request.queryParameters["includeVedtak"] == "true"

        val hasAccessToEblanketter = isInGroup(groupsConfig.getString("pensjon_utland"))

        val redigerbareBrev = brevmetadataService.getRedigerbareBrev(sakType, includeVedtak)
        val eblanketter = if (hasAccessToEblanketter) brevmetadataService.getEblanketter() else emptyList()

        call.respond(redigerbareBrev + eblanketter)
    }
}