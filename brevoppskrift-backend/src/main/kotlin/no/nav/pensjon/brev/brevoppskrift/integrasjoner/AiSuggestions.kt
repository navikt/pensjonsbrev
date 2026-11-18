package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeout
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.api.available

class AiSuggestions(private val service: AiService?) {
    private val limit = Semaphore(2)
    val enabled: Boolean get() = service != null

    suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
        val service = available(service)
        if (!limit.tryAcquire()) throw ApiFailure(HttpStatusCode.TooManyRequests, "AI_BUSY", "KI-tjenesten er opptatt.")
        try {
            return withTimeout(30_000) { service.suggest(request, nynorsk) }
        } finally {
            limit.release()
        }
    }
}
