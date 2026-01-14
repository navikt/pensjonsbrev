package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import io.ktor.utils.io.CancellationException
import io.valkey.DefaultJedisClientConfig
import io.valkey.HostAndPort
import io.valkey.JedisPool
import io.valkey.params.SetParams
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

val defaultTtl = 10.minutes

abstract class Cache() {
    val objectMapper = databaseObjectMapper
    abstract suspend fun read(key: String): String?
    abstract suspend fun update(key: String, value: String, ttl: Duration)
}

suspend inline fun <K, reified V> Cache.cached(
    omraade: Cacheomraade,
    key: K,
    noinline ttl: (V) -> Duration = { defaultTtl },
    noinline fetch: suspend () -> V,
): V {
    val serializedKey = "${omraade.prefix}-${objectMapper.writeValueAsString(key)}"
    return read(serializedKey)?.let { objectMapper.readValue(it) }
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

class Valkey(config: Config) : Cache() {
    private val logger = LoggerFactory.getLogger(Valkey::class.java)

    private val jedisPool = setupJedis(config)

    override suspend fun read(key: String): String? = try {
        jedisPool.resource.use {
            retryOgPakkUt(times = 3, ventetid = 50.milliseconds) { it.get(key) }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        logger.info("Fikk feilmelding fra Valkey under forsøk på å hente verdi, returnerer null", e)
        null
    }

    override suspend fun update(key: String, value: String, ttl: Duration) {
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
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.info("Fikk feilmelding fra Valkey under forsøk på å oppdatere verdi", e)
        }
    }

    private fun setupJedis(config: Config): JedisPool {
        val host = config.getString("host")
        val port = config.getString("port").toInt()
        val username = config.getString("username")
        val password = config.getString("password")
        val ssl = config.getBoolean("ssl")

        return JedisPool(
            HostAndPort(host, port),
            DefaultJedisClientConfig.builder()
                .ssl(ssl)
                .user(username)
                .password(password)
                .build()
        )
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

    private data class Value(val invalidAt: TimeMark, val value: String)
}

enum class Cacheomraade(val prefix: String) {
    AD("AD"),
    ALLTID_VALGBARE_VEDLEGG("ALLTID_VALGBARE_VEDLEGG"),
    NAVANSATT("Navansatt"),
    NAVANSATTENHET("NavAnsattEnhet"),
    NORG("Norg"),
    SKJERMING("Skjerming"),
    REDIGERBAR_MAL("Redigerbar"),
    SAMHANDLER("Samhandler"),
    SAMHANDLER_ADRESSE("SamhandlerAdresse"),
    PENSJON_REPRESENTASJON("PensjonRepresentasjon"),
}