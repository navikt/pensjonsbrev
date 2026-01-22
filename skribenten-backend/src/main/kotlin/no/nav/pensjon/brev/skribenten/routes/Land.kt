package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.brev.Landkoder

fun Route.landRoute() {
    get("/land") {
        call.respond(Landkoder.landkoderMedNavn)
    }
    get("/landForP1") {
        call.respond((Landkoder.eoesLand + Landkoder.nordiskTrygdeavtaleland + Landkoder.trygdeavtaleMedStorbritanniaLand).distinct())
    }
}