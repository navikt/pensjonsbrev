package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    penService: PenService,
    navansattService: NavansattService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
) {
    route("/sak/{sakId}") {
        install(AuthorizeAnsattSakTilgang(navansattService, pdlService, penService))

        get("") {
            val sakId = call.parameters.getOrFail("sakId")
            respondWithResult(penService.hentSak(call, sakId))
        }
        post("/bestillbrev") {
            val request = call.receive<LegacyBrevService.OrderLetterRequest>()
            call.respond(legacyBrevService.bestillBrev(call, request))
        }
        post<PidRequest>("/navn") {
            respondWithResult(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/adresse") {
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/foretrukketSpraak") {
            call.respond(krrService.getPreferredLocale(call, it.pid))
        }
    }
}

data class PidRequest(val pid: String)
