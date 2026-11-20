package no.nav.pensjon.brev.brevoppskrift.api

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveChannel
import io.ktor.utils.io.readAvailable
import no.nav.pensjon.brev.brevoppskrift.MAX_DRAFT_BYTES
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.lagring.MissingDraft
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.io.ByteArrayOutputStream
import java.util.UUID

fun validate(draft: Draft) {
    val issues = DraftValidator.validate(draft)
    if (issues.isNotEmpty()) throw InvalidDraft(issues)
}

fun ApplicationCall.draftId(): DraftId = runCatching { DraftId(UUID.fromString(parameters["id"])) }.getOrElse { throw MissingDraft() }
fun <T : Any> available(service: T?): T = service ?: throw ApiFailure(HttpStatusCode.ServiceUnavailable, "UNAVAILABLE", "Funksjonen er ikke konfigurert.")

suspend inline fun <reified T : Any> ApplicationCall.readJson(limit: Int = MAX_DRAFT_BYTES): T {
    if (request.contentType().withoutParameters() != ContentType.Application.Json) {
        throw ApiFailure(HttpStatusCode.UnsupportedMediaType, "UNSUPPORTED_MEDIA_TYPE", "Forventet application/json.")
    }
    val channel = receiveChannel()
    val buffer = ByteArray(8192)
    val body = ByteArrayOutputStream()
    while (true) {
        val read = channel.readAvailable(buffer)
        if (read == -1) break
        if (body.size() + read > limit) throw ApiFailure(HttpStatusCode.PayloadTooLarge, "DRAFT_TOO_LARGE", "Forespørselen er for stor.")
        body.write(buffer, 0, read)
    }
    return try {
        draftObjectMapper.readValue(body.toByteArray(), T::class.java)
    } catch (_: JacksonException) {
        throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_DRAFT", "Ugyldig forespørselsformat.")
    }
}
