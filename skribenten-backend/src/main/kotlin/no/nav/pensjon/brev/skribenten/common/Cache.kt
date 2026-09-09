package no.nav.pensjon.brev.skribenten.common

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.utils.io.*
import io.ktor.utils.io.core.Closeable
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.valkey.*
import io.valkey.params.SetParams
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import no.nav.pensjon.brev.skribenten.*
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.use
import kotlin.time.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

val defaultTtl = 10.minutes

sealed class Cache : Closeable {
    val objectMapper = databaseObjectMapper
    abstract suspend fun read(key: String): String?
    abstract suspend fun update(key: String, value: String, ttl: Duration)
}

private val factoryLogger = LoggerFactory.getLogger(Cache::class.java)
fun cacheFactory(config: SkribentenConfig, registry: MeterRegistry = Metrics.registry): Cache =
    if (config.valkey.enabled) {
        Valkey(config.valkey, registry)
    } else {
        factoryLogger.warn("Valkey is disabled, this is not recommended for production")
        InMemoryCache()
    }

suspend inline fun <K, reified V> Cache.cached(
    omraade: Cacheomraade,
    key: K,
    noinline ttl: (V) -> Duration = { defaultTtl },
    noinline fetch: suspend () -> V,
): V {
    val serializedKey = "${omraade.prefix}-${objectMapper.writeValueAsString(key)}"
    return read(serializedKey)?.let { objectMapper.readValue<V>(it) }
        ?: fetch().also {
            if (it == null) {
                return@also
            }
            val timeToLive = ttl(it)
            if (timeToLive.isPositive()) {
                update(
                    serializedKey,
                    objectMapper.writeValueAsString(it),
                    timeToLive,
                )
            }
        }
}

class Valkey(config: ValkeyConfig, private val registry: MeterRegistry = Metrics.registry) : Cache() {
    private val logger = LoggerFactory.getLogger(Valkey::class.java)
    private val jedisPool = setupJedis(config)

    private fun recordOperation(sample: Timer.Sample, operation: String, outcome: String) {
        sample.stop(
            Timer.builder(Metrics.cacheOperationMetricName)
                .description("Varighet og utfall av kall mot Valkey")
                .tag("operation", operation)
                .tag("outcome", outcome)
                .register(registry)
        )
    }

    // Cacheomraade sitt prefiks (satt av `cached()`) ligger alltid først i nøkkelen, adskilt med "-".
    private fun recordLookup(key: String, result: String) {
        Counter.builder("skribenten_cache_requests")
            .description("Antall cache-oppslag per cacheområde, fordelt på treff og bom.")
            .tag("omraade", key.substringBefore("-"))
            .tag("result", result)
            .register(registry)
            .increment()
    }

    override suspend fun read(key: String): String? {
        val sample = Timer.start(registry)
        return try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3, ventetid = 50.milliseconds) { it.get(key) }
            }.also { value ->
                recordOperation(sample, "read", "success")
                recordLookup(key, if (value != null) "hit" else "miss")
            }
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            recordOperation(sample, "read", "error")
            logger.info("Fikk feilmelding fra Valkey under forsøk på å hente verdi, returnerer null", e)
            null
        }
    }

    override suspend fun update(key: String, value: String, ttl: Duration) {
        val sample = Timer.start(registry)
        try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3, ventetid = 50.milliseconds) {
                    it.set(
                        key,
                        value,
                        SetParams().apply {
                            ex(ttl.inWholeSeconds)
                        },
                    )
                }
            }
            recordOperation(sample, "update", "success")
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            recordOperation(sample, "update", "error")
            logger.info("Fikk feilmelding fra Valkey under forsøk på å oppdatere verdi", e)
        }
    }

    private fun setupJedis(config: ValkeyConfig): JedisPool = with(config) {
        JedisPool(
            JedisPoolConfig().apply {
                testOnBorrow = true
                testWhileIdle = true
            },
            HostAndPort(host, port),
            DefaultJedisClientConfig.builder()
                .ssl(ssl)
                .user(username)
                .password(password)
                .build()
        )
    }

    override fun close() {
        jedisPool.close()
    }
}

class InMemoryCache : Cache() {
    private val timesource = TimeSource.Monotonic
    private val cache = ConcurrentHashMap<String, Value>()

    override suspend fun read(key: String): String? {
        cache.filter { it.value.invalidAt.hasPassedNow() }.forEach { cache.remove(it.key) }

        return cache[key]
            ?.takeIf { it.invalidAt.hasNotPassedNow() }
            ?.value
    }

    override suspend fun update(key: String, value: String, ttl: Duration) {
        cache[key] = Value(timesource.markNow() + ttl, value)
    }

    override fun close() {
        cache.clear()
    }

    private data class Value(val invalidAt: TimeMark, val value: String)
}

enum class Cacheomraade(val prefix: String) {
    AD("AD"),
    ALLTID_VALGBARE_VEDLEGG("ALLTID_VALGBARE_VEDLEGG"),
    FAGSAK("fagsak"),
    NAVANSATT("Navansatt"),
    NAVANSATTENHET("NavAnsattEnhet"),
    NORG("Norg"),
    SKJERMING("Skjerming"),
    REDIGERBAR_MAL("Redigerbar"),
    HAR_REDIGERBARE_VEDLEGG("HarRedigerbareVedlegg"),
    SAMHANDLER("Samhandler"),
    SAMHANDLER_ADRESSE("SamhandlerAdresse"),
    PENSJON_REPRESENTASJON("PensjonRepresentasjon"),
    PENSJON_PERSONDATA("PensjonPersondata"),
}