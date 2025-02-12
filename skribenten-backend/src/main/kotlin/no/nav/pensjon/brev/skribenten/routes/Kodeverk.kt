package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Route.kodeverkRoute(penService: PenService) {
    route("/kodeverk") {
        install(CachingHeaders) {
            options { _, _ -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 86400)) }
        }
        get("/avtaleland") {
            respondWithResult(penService.hentAvtaleland())
        }
    }
}
