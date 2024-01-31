package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.OrderLetterRequest

fun Route.bestillBrevRoute(
    legacyBrevService: LegacyBrevService
) {

    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        call.respond(legacyBrevService.bestillBrev(call, request))
    }
}
