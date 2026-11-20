package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpStatusCode
import io.ktor.http.ContentType
import io.ktor.server.auth.principal
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.describe
import io.ktor.openapi.jsonSchema
import io.ktor.utils.io.ExperimentalKtorApi
import no.nav.pensjon.brev.brevoppskrift.Capabilities
import no.nav.pensjon.brev.brevoppskrift.auth.DraftUser
import no.nav.pensjon.brev.brevoppskrift.auth.requireEditor
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.model.*

data class GenerateRequest(val draft: Draft)
class InvalidDraft(val issues: List<ValidationIssue>) : RuntimeException("INVALID_DRAFT")

@OptIn(ExperimentalKtorApi::class)
fun Route.authoringRoutes(catalogue: SourceCatalogue) {
    val generator: DraftGenerator by application.dependencies
    val preview: DraftPreview by application.dependencies
    val ai: AiSuggestions by application.dependencies
    val notifications: DraftNotifications by application.dependencies
    route("/api/v1") {
        get("/meg") { call.respond(requireNotNull(call.principal<DraftUser>())) }
        get("/integrasjoner") {
            call.respond(Capabilities(formatVersion = DRAFT_FORMAT_VERSION, persistence = true, generation = true, ai = ai.enabled, slack = notifications.enabled))
        }
        get("/katalog") { call.respond(catalogue.list()) }
        get("/katalog/{sourceId}") {
            call.respond(catalogue.import(SourceId(requireNotNull(call.parameters["sourceId"]))) ?: throw ApiFailure(HttpStatusCode.NotFound, "SOURCE_NOT_FOUND", "Fant ikke malen."))
        }
        post("/generer") { call.respond(generator.generate(call.readJson<GenerateRequest>().draft)) }
            .describe { requestBody { schema = jsonSchema<GenerateRequest>(); required = true } }
        post("/forhandsvisning") { call.respond(preview.render(call.readJson<PreviewRequest>())) }
            .describe { requestBody { schema = jsonSchema<PreviewRequest>(); required = true } }
        post("/ki/nynorsk") { call.requireEditor(); call.respond(ai.suggest(call.readJson<AiRequest>(200 * 1024), nynorsk = true)) }
            .describe { requestBody { schema = jsonSchema<AiRequest>(); required = true } }
        post("/ki/klarsprak") { call.requireEditor(); call.respond(ai.suggest(call.readJson<AiRequest>(200 * 1024), nynorsk = false)) }
            .describe { requestBody { schema = jsonSchema<AiRequest>(); required = true } }
        draftRoutes()
    }.describe {
        val problemContentType = ContentType.parse("application/problem+json")
        responses {
            for (status in listOf(
                HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden,
                HttpStatusCode.NotFound, HttpStatusCode.Gone, HttpStatusCode.Conflict,
                HttpStatusCode.PayloadTooLarge, HttpStatusCode.UnsupportedMediaType,
                HttpStatusCode.UnprocessableEntity, HttpStatusCode.TooManyRequests,
                HttpStatusCode.InternalServerError, HttpStatusCode.ServiceUnavailable,
            )) {
                status {
                    problemContentType { schema = jsonSchema<Problem>() }
                }
            }
        }
    }
}
