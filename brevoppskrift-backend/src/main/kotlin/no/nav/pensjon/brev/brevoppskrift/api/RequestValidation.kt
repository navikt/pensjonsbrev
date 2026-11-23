package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import no.nav.pensjon.brev.brevoppskrift.lagring.MissingDraft
import no.nav.pensjon.brev.brevoppskrift.model.*
import java.util.UUID

fun validate(draft: Draft) {
    val issues = DraftValidator.validate(draft)
    if (issues.isNotEmpty()) throw InvalidDraft(issues)
}

fun ApplicationCall.draftId(): DraftId = runCatching { DraftId(UUID.fromString(parameters["id"])) }.getOrElse { throw MissingDraft() }
fun <T : Any> available(service: T?): T = service ?: throw ApiFailure(HttpStatusCode.ServiceUnavailable, "UNAVAILABLE", "Funksjonen er ikke konfigurert.")
