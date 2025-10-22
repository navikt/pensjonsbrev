package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import io.valkey.DefaultJedisClientConfig
import io.valkey.HostAndPort
import io.valkey.JedisPool
import io.valkey.params.SetParams
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

val defaultTtl = 10.minutes

abstract class Cache(val objectMapper: ObjectMapper) {
    abstract fun <K, V> get(
        omraade: Cacheomraade,
        key: K,
        clazz: Class<V>,
        deserialize: (String) -> V,
    ): V?
    abstract fun <K, V> update(
        omraade: Cacheomraade,
        key: K,
        value: V,
        ttl: Duration,
    )
}

suspend inline fun <K, reified V> Cache.cached(omraade: Cacheomraade, key: K, noinline ttl: (V) -> Duration = { defaultTtl },
                                               noinline fetch: suspend (K) -> V?): V? {
    return get(omraade, key, V::class.java, { objectMapper.readValue(it) }) ?: fetch(key)?.also {
        val timeToLive = ttl(it)
        if (timeToLive.isPositive()) {
            update(omraade, key, it, ttl(it))
        }
    }
}

class Valkey(
    config: Config,
    objectMapper: ObjectMapper = databaseObjectMapper,
) : Cache(objectMapper) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val jedisPool = setupJedis(config)

    override fun <K, V> get(omraade: Cacheomraade, key: K, clazz: Class<V>, deserialize: (String) -> V): V? =
        try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3) { it.get(objectMapper.writeWithPrefix(omraade, key)) }
                    ?.let { v -> deserialize(v) }
            }
        } catch (e: Exception) {
            logger.warn("Fikk feilmelding fra Valkey under forsøk på å hente verdi, returnerer null", e)
            null
        }

    override fun <K, V> update(omraade: Cacheomraade, key: K, value: V, ttl: Duration) {
        try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3) {
                    it.set(
                        objectMapper.writeWithPrefix(omraade, key),
                        objectMapper.writeValueAsString(value),
                        SetParams().apply {
                            ex(ttl.inWholeSeconds)
                        },
                    )
                }
            }
        } catch (e: Exception) {
            logger.warn("Fikk feilmelding fra Valkey under forsøk på å oppdatere verdi", e)
            return
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

class InMemoryCache : Cache(databaseObjectMapper) {
    private val timesource = TimeSource.Monotonic
    private val cache = ConcurrentHashMap<String, Value<String>>()

    override fun <K, V> get(omraade: Cacheomraade, key: K, clazz: Class<V>, deserialize: (String) -> V): V? {
        cache.filter { it.value.invalidAt.hasPassedNow() }.forEach { cache.remove(it.key) }

        return cache[objectMapper.writeWithPrefix(omraade, key)]
            ?.takeIf { it.invalidAt.hasNotPassedNow() }
            ?.let { deserialize(it.value) }
    }

    override fun <K, V> update(omraade: Cacheomraade, key: K, value: V, ttl: Duration) {
        cache[objectMapper.writeWithPrefix(omraade, key)] = Value(timesource.markNow() + ttl, objectMapper.writeValueAsString(value))
    }

    private data class Value<V : Any>(val invalidAt: TimeMark, val value: V)
}

private fun <T> ObjectMapper.writeWithPrefix(omraade: Cacheomraade, key: T): String = writeValueAsString(omraade.prefix + "-" + writeValueAsString(key))

enum class Cacheomraade(val prefix: String) {
    AD("AD"),
    NAVANSATT("Navansatt"),
    NAVANSATTENHET("NavAnsattEnhet"),
    NORG("Norg"),
    REDIGERBAR_MAL("Redigerbar"),
    SAMHANDLER("Samhandler"),
}