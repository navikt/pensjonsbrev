package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.pensjon.brev.skribenten.model.Landkoder

fun Route.landRoute() {
    get("/land") {
        call.respond(Landkoder.landkoderMedNavn)
    }
}