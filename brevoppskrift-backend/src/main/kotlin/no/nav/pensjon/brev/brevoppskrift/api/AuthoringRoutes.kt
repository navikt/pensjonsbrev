package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.plugins.bodylimit.RequestBodyLimit
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.auth.principal
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.brevoppskrift.MAX_DRAFT_BYTES
import no.nav.pensjon.brev.brevoppskrift.Capabilities
import no.nav.pensjon.brev.brevoppskrift.auth.DraftUser
import no.nav.pensjon.brev.brevoppskrift.auth.editorRoutes
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.model.*

data class GenerateRequest(val draft: Draft)
class InvalidDraft(val issues: List<ValidationIssue>) : RuntimeException("INVALID_DRAFT")

fun Route.authoringRoutes(catalogue: SourceCatalogue) {
    val generator: DraftGenerator by application.dependencies
    val preview: DraftPreview by application.dependencies
    val ai: AiSuggestions by application.dependencies
    val notifications: DraftNotifications by application.dependencies
    route("/api/v1") {
        install(RequestBodyLimit) {
            bodyLimit { call ->
                when (call.request.path()) {
                    "/api/v1/ki/nynorsk", "/api/v1/ki/klarsprak" -> 200L * 1024
                    else -> MAX_DRAFT_BYTES.toLong()
                }
            }
        }
        get("/meg") { call.respond(requireNotNull(call.principal<DraftUser>())) }
        get("/integrasjoner") {
            call.respond(Capabilities(formatVersion = DRAFT_FORMAT_VERSION, persistence = true, generation = true, ai = ai.enabled, slack = notifications.enabled))
        }
        get("/katalog") { call.respond(catalogue.list()) }
        get("/katalog/{sourceId}") {
            call.respond(catalogue.import(SourceId(requireNotNull(call.parameters["sourceId"]))) ?: throw ApiFailure(HttpStatusCode.NotFound, "SOURCE_NOT_FOUND", "Fant ikke malen."))
        }
        post("/generer") { call.respond(generator.generate(call.receive<GenerateRequest>().draft)) }
        post("/forhandsvisning") { call.respond(preview.render(call.receive<PreviewRequest>())) }
        editorRoutes {
            post("/ki/nynorsk") { call.respond(ai.suggest(call.receive<AiRequest>(), nynorsk = true)) }
            post("/ki/klarsprak") { call.respond(ai.suggest(call.receive<AiRequest>(), nynorsk = false)) }
        }
        draftRoutes()
    }
}
