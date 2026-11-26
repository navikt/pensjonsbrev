package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

class AiSuggestionsTest {
    @Test
    fun `caller timeout remains cancellation and releases permit`(): Unit = runBlocking {
        var wait = true
        val suggestions = AiSuggestions(object : AiService {
            override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
                if (wait) awaitCancellation()
                return AiResult(request.text)
            }
        })
        val request = AiRequest("synthetic", DraftLanguage.NB)
        val failure = runCatching { withTimeout(50.milliseconds) { suggestions.suggest(request, false) } }.exceptionOrNull()
        assertThat(failure).isInstanceOf(TimeoutCancellationException::class.java)
        wait = false
        assertThat(suggestions.suggest(request, false).text).isEqualTo(request.text)
    }

    @Test
    fun `AI concurrency is bounded and permits are released after cancellation and failure`(): Unit = runBlocking {
        val entered = kotlinx.coroutines.channels.Channel<Unit>(2)
        val release = CompletableDeferred<Unit>()
        val service = object : AiService {
            override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
                entered.send(Unit)
                release.await()
                return AiResult(request.text)
            }
        }
        val suggestions = AiSuggestions(service)
        val request = AiRequest("synthetic", DraftLanguage.NB)
        val first = async { suggestions.suggest(request, true) }
        val second = async { suggestions.suggest(request, false) }
        withTimeout(5.seconds) { repeat(2) { entered.receive() } }
        assertThat((runCatching { suggestions.suggest(request, false) }.exceptionOrNull() as ApiFailure).code).isEqualTo("AI_BUSY")
        first.cancelAndJoin()
        val replacement = async { suggestions.suggest(request, false) }
        withTimeout(5.seconds) { entered.receive() }
        release.complete(Unit)
        assertThat(second.await().text).isEqualTo("synthetic")
        assertThat(replacement.await().text).isEqualTo("synthetic")
        entered.close()
        val failing = AiSuggestions(object : AiService {
            override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult = throw IntegrationFailure("VERTEX_UNAVAILABLE")
        })
        repeat(3) { assertThat(runCatching { failing.suggest(request, false) }.exceptionOrNull()).isInstanceOf(IntegrationFailure::class.java) }
    }

}
