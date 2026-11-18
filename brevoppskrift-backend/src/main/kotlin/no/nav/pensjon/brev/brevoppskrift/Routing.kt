package no.nav.pensjon.brev.brevoppskrift

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.brevoppskrift.api.authoringRoutes
import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.auth.ENTRA_AUTH
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRepository

fun Application.configureRouting() {
    val repository: DraftRepository by dependencies
    val metrics: PrometheusMeterRegistry by dependencies
    val catalogue: SourceCatalogue by dependencies
    routing {
        get("/isAlive") { call.respondText("Alive") }
        get("/isReady") {
            if (repository.ready()) call.respondText("Ready")
            else call.respondText("Unavailable", status = HttpStatusCode.ServiceUnavailable)
        }
        get("/metrics") { call.respondText(metrics.scrape(), ContentType.parse("text/plain; version=0.0.4")) }
        authenticate(ENTRA_AUTH) { authoringRoutes(catalogue) }
    }
}
