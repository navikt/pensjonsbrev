package no.nav.pensjon.brev.brevoppskrift

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.CancellationException
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.api.InvalidDraft
import no.nav.pensjon.brev.brevoppskrift.api.respondProblem
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.IntegrationFailure
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import org.slf4j.LoggerFactory

fun Application.configureErrors(metrics: MeterRegistry) {
    val logger = LoggerFactory.getLogger("brevoppskrift")
    install(StatusPages) {
        exception<IntegrationFailure> { call, cause -> call.respondProblem(HttpStatusCode.ServiceUnavailable, cause.code, "Integrasjonen kunne ikke svare. Forslaget eller PR-en er ikke godkjent.") }
        exception<InvalidDraft> { call, cause -> call.respondProblem(HttpStatusCode.UnprocessableEntity, "INVALID_DRAFT", "Utkastet har valideringsfeil.", cause.issues) }
        exception<RevisionConflict> { call, cause ->
            metrics.counter("brevoppskrift.draft.conflicts").increment()
            call.respondProblem(HttpStatusCode.Conflict, "REVISION_CONFLICT", "Noen andre har lagret. Dine endringer er ikke overskrevet.", currentRevision = cause.currentRevision)
        }
        exception<SourceConflict> { call, _ -> call.respondProblem(HttpStatusCode.Conflict, "SOURCE_CONFLICT", "Malen har allerede et aktivt arbeidsutkast.") }
        exception<MissingDraft> { call, _ -> call.respondProblem(HttpStatusCode.NotFound, "DRAFT_NOT_FOUND", "Fant ikke utkastet.") }
        exception<ArchivedDraft> { call, _ -> call.respondProblem(HttpStatusCode.Gone, "DRAFT_ARCHIVED", "Utkastet er arkivert.") }
        exception<java.sql.SQLException> { call, _ -> call.respondProblem(HttpStatusCode.ServiceUnavailable, "DATABASE_UNAVAILABLE", "Lagringen er ikke tilgjengelig.") }
        exception<ApiFailure> { call, cause -> call.respondProblem(cause.status, cause.code, cause.detail) }
        exception<BadRequestException> { call, _ -> call.respondProblem(HttpStatusCode.BadRequest, "INVALID_REQUEST", "Ugyldig forespørsel.") }
        exception<Exception> { call, cause ->
            if (cause is CancellationException) throw cause
            logger.error("Request failed: {}", cause.javaClass.simpleName)
            call.respondProblem(HttpStatusCode.InternalServerError, "INTERNAL_ERROR", "Forespørselen kunne ikke behandles.")
        }
        status(HttpStatusCode.NotFound) { call, _ -> call.respondProblem(HttpStatusCode.NotFound, "NOT_FOUND", "Fant ikke API-ruten.") }
        status(HttpStatusCode.MethodNotAllowed) { call, _ -> call.respondProblem(HttpStatusCode.MethodNotAllowed, "METHOD_NOT_ALLOWED", "Metoden støttes ikke.") }
    }
}
