package no.nav.pensjon.brev.skribenten.routes

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.isInGroup
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService,
    groupsConfig: Config
) {
    get("/lettertemplates/e-blanketter") {
        val hasAccess = isInGroup(groupsConfig.getString("pensjon_utland"))
        val eblanketter = if (hasAccess) brevmetadataService.getEblanketter() else emptyList()
        call.respond(eblanketter)
    }

    get("/lettertemplates/{sakType}") {
        val sakType = call.parameters.getOrFail("sakType")
        val includeVedtak = call.request.queryParameters["includeVedtak"] == "true"
        call.respond(
            brevmetadataService.getRedigerbareBrev(sakType, includeVedtak),
        )
    }
}