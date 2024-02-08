package no.nav.pensjon.brev.skribenten.routes

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.isInGroup
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import org.slf4j.LoggerFactory

fun Route.brevmalerRoute(
    brevmetadataService: BrevmetadataService,
    groupsConfig: Config
) {
    val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.routes.brevmalerRoute")
    get("/lettertemplates/e-blanketter") {
        logger.info("DEBUG_TMP: ${groupsConfig.getString("pensjon_utland")}")
        logger.info(
            "DEBUG_TMP2: ${
                call.authentication.principal<UserPrincipal>()?.jwtPayload?.getClaim("groups")
                    ?.asList(String::class.java)
            }"
        )
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