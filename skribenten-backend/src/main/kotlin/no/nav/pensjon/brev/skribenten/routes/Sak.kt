package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.respondWithResult
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Route::class.java)

fun Route.sakRoute(
    penService: PenService,
    navansattService: NavansattService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
) {
    route("/sak") {
        intercept(ApplicationCallPipeline.Call) {
            sjekkEnhetstilgangTilSak(navansattService)
        }
        get("/{sakId}/") {
            val sakId = call.parameters.getOrFail("sakId")
            respondWithResult(penService.hentSak(call, sakId))
        }
        post("/{sakId}/bestillbrev") {
            val request = call.receive<LegacyBrevService.OrderLetterRequest>()
            call.respond(legacyBrevService.bestillBrev(call, request))
        }
        post<PidRequest>("/{sakId}/navn") {
            respondWithResult(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/{sakId}/adresse") {
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/{sakId}/foretrukketSpraak") {
            call.respond(krrService.getPreferredLocale(call, it.pid))
        }
    }
}

data class PidRequest(val pid: String)

suspend fun PipelineContext<Unit, ApplicationCall>.sjekkEnhetstilgangTilSak(navansattService: NavansattService) {
    val sakId = call.parameters["sakId"].toString()
    if (sakId.isEmpty()) {
        call.respond(HttpStatusCode.BadRequest, "SakId mangler i request")
        return
    }
    if (!navansattService.harAnsattTilgangTilEnhet(call, sakId)) {
        val message = "Navansatt: ${fetchLoggedInNavIdent(call)} har ikke enhetstilgang til sak"
        logger.error(message)
        call.respond(HttpStatusCode.Forbidden, message)
    }
}

private fun fetchLoggedInNavIdent(call: ApplicationCall): String {
    return call.getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
}
