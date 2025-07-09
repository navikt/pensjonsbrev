package no.nav.pensjon.brev.skribenten

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class Cache<K : Any, V : Any>(private val ttl: Duration = 10.minutes) {
    private val timesource = TimeSource.Monotonic
    private val cache = ConcurrentHashMap<K, Value<V>>()
    private val cleanupLock = Mutex()

    private data class Value<V : Any>(val invalidAt: TimeMark, val value: V)

    suspend fun getValue(key: K, fetch: suspend (K) -> V?): V? {
        tryClearExpired()

        return cache[key]?.takeIf { it.invalidAt.hasNotPassedNow() }?.value
            ?: fetch(key)?.also { cache[key] = Value(timesource.markNow() + ttl, it) }
    }

    suspend fun cached(key: K, fetch: suspend (K) -> V?): V? =
        getValue(key, fetch)

    // Clear expired values from cache.
    // Will only allow one actor to perform cleanup at any one time.
    private fun tryClearExpired() {
        if (cleanupLock.tryLock()) {
            try {
                cache.filter { it.value.invalidAt.hasPassedNow() }
                    .forEach { cache.remove(it.key) }
            } finally {
                cleanupLock.unlock()
            }
        }
    }
}
