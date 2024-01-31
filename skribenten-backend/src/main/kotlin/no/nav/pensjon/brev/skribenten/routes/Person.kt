package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.*


fun Route.personRoute(pdlService: PdlService, pensjonPersonDataService: PensjonPersonDataService, krrService: KrrService) {

    route("/person") {
        post<PidRequest>("/navn") {
            respondWithResult2(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/adresse") {
            respondWithResult2(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/foretrukketSpraak") {
            call.respond(krrService.getPreferredLocale(call, it.pid))
        }
    }
}

data class PidRequest(val pid: String)