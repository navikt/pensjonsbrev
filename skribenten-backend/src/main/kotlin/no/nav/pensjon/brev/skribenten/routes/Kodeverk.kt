package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService

context(app: Application)
fun Route.kodeverkRoute() {
    route("/kodeverk") {
        val fagsakService: FagsakService by app.dependencies

        install(CachingHeaders) {
            options { _, _ -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 86400)) }
        }
        get("/avtaleland") {
            call.respond(fagsakService.hentAvtaleland())
        }
    }
}