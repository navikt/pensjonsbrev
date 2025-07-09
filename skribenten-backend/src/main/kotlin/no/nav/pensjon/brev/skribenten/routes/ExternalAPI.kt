package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.ExternalAPIService

fun Route.externalAPI(externalAPIService: ExternalAPIService) =
    route("/external/api/v1") {
        get("/brev") {
            val saksIder = call.queryParameters.getAll("saksId")
                ?.flatMap { it.split(",") }
                ?.mapNotNull { it.toLongOrNull() }
                ?: emptyList()

            call.respond(externalAPIService.hentAlleBrevForSaker(saksIder.toSet()))
        }
    }
