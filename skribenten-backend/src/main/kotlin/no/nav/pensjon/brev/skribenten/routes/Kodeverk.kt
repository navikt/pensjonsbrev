package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.PenService

fun Route.kodeverkRoute(penService: PenService) {
    route("/kodeverk") {

        install(CachingHeaders) {
            options { _, _ -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 86400)) }
        }
        get("/avtaleland") {
            call.respond(penService.hentAvtaleland())
        }
    }
}