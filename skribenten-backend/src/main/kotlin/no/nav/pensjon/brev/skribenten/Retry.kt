package no.nav.pensjon.brev.skribenten

import kotlin.coroutines.cancellation.CancellationException


fun <T> retryOgPakkUt(times: Int, block: () -> T) = retryInner(times, emptyList(), block).getOrThrow()

private fun <T> retryInner(times: Int, exceptions: List<Exception>, block: () -> T): Result<T> = try {
    Result.success(block())
} catch (ex: Exception) {
    if (ex is CancellationException) throw ex
    if (times < 1) {
        Result.failure(ex.also { exceptions.forEach { ex.addSuppressed(it) } })
    } else {
        retryInner(times - 1, exceptions + ex, block)
    }
}