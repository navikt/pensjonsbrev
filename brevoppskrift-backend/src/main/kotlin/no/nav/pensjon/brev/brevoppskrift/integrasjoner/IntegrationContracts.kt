package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage

class IntegrationFailure(val code: String) : RuntimeException(code)
data class AiRequest(val text: String, val language: DraftLanguage, val instruction: String = "")
data class AiResult(val text: String)
data class Notification(val type: String, val name: String, val count: Int = 0, val url: String? = null)

interface AiService {
    suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult
}

interface NotificationService {
    suspend fun send(notification: Notification)
}

interface PrService {
    suspend fun create(ticket: PrTicket): PrUrl
}
