package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import no.nav.pensjon.brev.brevoppskrift.model.ValidationIssue
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision

data class Problem(
    val type: String = "about:blank",
    val title: String,
    val status: Int,
    val code: String,
    val detail: String,
    val errors: List<ValidationIssue> = emptyList(),
    val currentRevision: DraftRevision? = null,
)

class ApiFailure(val status: HttpStatusCode, val code: String, val detail: String) : RuntimeException(code)

suspend fun ApplicationCall.respondProblem(
    status: HttpStatusCode,
    code: String,
    detail: String,
    errors: List<ValidationIssue> = emptyList(),
    currentRevision: DraftRevision? = null,
) {
    if (status == HttpStatusCode.Unauthorized) response.headers.append(HttpHeaders.WWWAuthenticate, "Bearer")
    respond(
        status,
        Problem(title = status.description, status = status.value, code = code, detail = detail, errors = errors, currentRevision = currentRevision),
    )
}
