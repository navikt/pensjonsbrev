package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AiSuggestionsTest {
    @Test
    fun `only two suggestions run concurrently and cancellation releases capacity`(): Unit = runBlocking {
        val started = Channel<Unit>(Channel.UNLIMITED)
        val release = CompletableDeferred<Unit>()
        val service = AiSuggestions(object : AiService {
            override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
                started.send(Unit)
                release.await()
                return AiResult(request.text)
            }
        })
        val request = AiRequest("synthetic", DraftLanguage.NB)
        val first = async { service.suggest(request, true) }
        val second = async { service.suggest(request, false) }
        try {
            withTimeout(5000) { repeat(2) { started.receive() } }
            val failure = runCatching { service.suggest(request, true) }.exceptionOrNull()
            assertThat(failure).isInstanceOf(ApiFailure::class.java)
            assertThat((failure as ApiFailure).status).isEqualTo(HttpStatusCode.TooManyRequests)
            first.cancelAndJoin()
            val replacement = async { service.suggest(request, true) }
            withTimeout(5000) { started.receive() }
            release.complete(Unit)
            assertThat(replacement.await().text).isEqualTo(request.text)
            assertThat(second.await().text).isEqualTo(request.text)
        } finally {
            release.complete(Unit)
            first.cancelAndJoin()
            second.cancelAndJoin()
        }
    }

    @Test
    fun `failed suggestions release capacity for subsequent calls`(): Unit = runBlocking {
        val service = AiSuggestions(object : AiService {
            override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult = throw IntegrationFailure("VERTEX_UNAVAILABLE")
        })
        repeat(3) {
            assertThat(runCatching { service.suggest(AiRequest("synthetic", DraftLanguage.NB), true) }.exceptionOrNull()).isInstanceOf(IntegrationFailure::class.java)
        }
    }
}
