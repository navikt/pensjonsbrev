package no.nav.pensjon.brev.brevoppskrift

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.openapi.OpenApiDocSource
import io.ktor.openapi.OpenApiInfo
import io.ktor.openapi.OpenApiDoc
import kotlinx.serialization.json.Json
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.brevoppskrift.api.authoringRoutes
import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.auth.ENTRA_AUTH
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRepository
import no.nav.pensjon.brev.brevoppskrift.openapi.JacksonSchemaInference
import no.nav.pensjon.brev.brevoppskrift.openapi.withCommonResponses

fun Application.configureRouting() {
    val repository: DraftRepository by dependencies
    val metrics: PrometheusMeterRegistry by dependencies
    val catalogue: SourceCatalogue by dependencies
    routing {
        swaggerUI("/swagger-internal") {
            info = OpenApiInfo("Brevoppskrift API", "1.0")
            source = OpenApiDocSource.Routing(
                contentType = ContentType.Application.Json,
                schemaInference = JacksonSchemaInference(draftObjectMapper),
                serializeModel = { Json.encodeToString(OpenApiDoc.serializer(), it.withCommonResponses()) },
                routes = { routingRoot.descendants().filter { it.path().startsWith("/api/v1") } },
            )
            remotePath = "documentation.json"
        }
        get("/isAlive") { call.respondText("Alive") }
        get("/isReady") {
            if (repository.ready()) call.respondText("Ready")
            else call.respondText("Unavailable", status = HttpStatusCode.ServiceUnavailable)
        }
        get("/metrics") { call.respondText(metrics.scrape(), ContentType.parse("text/plain; version=0.0.4")) }
        authenticate(ENTRA_AUTH) { authoringRoutes(catalogue) }
    }
}
