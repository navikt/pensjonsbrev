package no.nav.pensjon.brev.skribenten

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class Cache<K : Any, V : Any>(private val ttl: Duration = 10.minutes) {
    private val timesource = TimeSource.Monotonic
    private val cache = ConcurrentHashMap<K, Value<V>>()

    private data class Value<V : Any>(val invalidAt: TimeMark, val value: V)

    suspend fun getValue(
        key: K,
        fetch: suspend (K) -> V?,
    ): V? =
        cache[key]?.takeIf { it.invalidAt.hasNotPassedNow() }?.value
            ?: fetch(key)?.also { cache[key] = Value(timesource.markNow() + ttl, it) }

    suspend fun cached(
        key: K,
        fetch: suspend (K) -> V?,
    ): V? =
        getValue(key, fetch)
}
