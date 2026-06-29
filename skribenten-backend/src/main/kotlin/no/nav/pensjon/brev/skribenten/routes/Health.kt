package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.db.databaseReady

fun Route.healthRoute() {
    get("/isAlive") {
        call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
    }

    get("/isReady") {
        if (databaseReady.get()) {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.ServiceUnavailable, "Database not ready")
        }
    }
}