package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.time.Duration.Companion.seconds
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.api.available

class AiSuggestions(private val service: AiService?) {
    private val limit = Semaphore(2)
    val enabled: Boolean get() = service != null

    suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
        val service = available(service)
        validateAiRequest(request)
        if (!limit.tryAcquire()) throw ApiFailure(HttpStatusCode.TooManyRequests, "AI_BUSY", "KI-tjenesten er opptatt.")
        try {
            return withTimeout(30.seconds) { service.suggest(request, nynorsk) }
        } catch (_: TimeoutCancellationException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("VERTEX_UNAVAILABLE")
        } finally {
            limit.release()
        }
    }
}
