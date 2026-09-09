package no.nav.pensjon.brev.skribenten.common

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlin.time.Duration


suspend fun <T> retryOgPakkUt(times: Int, ventetid: Duration = Duration.ZERO, block: () -> T) = retryInner(times, emptyList(), ventetid, block).getOrThrow()

private suspend fun <T> retryInner(times: Int, exceptions: List<Exception>, ventetid: Duration, block: () -> T): Result<T> = try {
    Result.success(block())
} catch (ex: Exception) {
    currentCoroutineContext().ensureActive()
    if (times < 1) {
        Result.failure(ex.also { exceptions.forEach { ex.addSuppressed(it) } })
    } else {
        delay(ventetid)
        retryInner(times - 1, exceptions + ex, ventetid, block)
    }
}