package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Route.navansattRoute(navansattService: NavansattService) {
    route("/navansatt") {

        get("/{ansattId}/enheter") {
            val ansattId = call.parameters.getOrFail<String>("ansattId")
            respondWithResult(navansattService.hentNavAnsattEnhetListe(ansattId))
        }
    }

}