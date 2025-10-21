package no.nav.pensjon.brev.skribenten

import kotlin.coroutines.cancellation.CancellationException


sealed class RetryResult<T> {
    data class Success<T>(
        val content: T,
    ) : RetryResult<T>()

    data class Failure<T>(
        val exceptions: List<Exception> = emptyList(),
    ) : RetryResult<T>() {
        fun samlaExceptions(): Exception = exceptions.last().also { last ->
            exceptions.dropLast(1).reversed().forEach { last.addSuppressed(it) }
        }
    }
}

fun <T> retryOgPakkUt(times: Int, block: () -> T) = retryInner(times, emptyList(), block).let {
    when (it) {
        is RetryResult.Success -> it.content
        is RetryResult.Failure -> throw it.samlaExceptions()
    }
}

private fun <T> retryInner(times: Int, exceptions: List<Exception>, block: () -> T): RetryResult<T> = try {
    RetryResult.Success(block())
} catch (ex: Exception) {
    if (ex is CancellationException) throw ex
    if (times < 1) {
        RetryResult.Failure(exceptions + ex)
    } else {
        retryInner(times - 1, exceptions + ex, block)
    }
}