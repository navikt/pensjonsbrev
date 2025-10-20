package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import io.valkey.DefaultJedisClientConfig
import io.valkey.HostAndPort
import io.valkey.JedisPool
import io.valkey.params.SetParams
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

private val defaultTtl = 1.hours

interface Cache {
    fun <K, V> get(key: K, clazz: Class<V>): V?
    fun <K, V> update(key: K, value: V, ttl: Duration = defaultTtl)
    suspend fun <K, V> cached(key: K, clazz: Class<V>, ttl: Duration = defaultTtl, fetch: suspend (K) -> V?): V? =
        get(key, clazz) ?: fetch(key)?.also { update(key, it, ttl) }
}

class Valkey(config: Map<String, String?>, instanceName: String, private val objectMapper: ObjectMapper = databaseObjectMapper) : Cache {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val jedisPool = setupJedis(config, instanceName.uppercase())

    override fun <K, V> get(key: K, clazz: Class<V>): V? =
        try {
            logger.info("Henter verdi for {}", key)
            jedisPool.resource.use { it.get(objectMapper.writeValueAsString(key))?.let { k -> objectMapper.readValue(k, clazz) } }.also {
                logger.info("Hentet verdi for {}: {}", key, it)
            } ?: logger.info("Fant ingen verdi for {}", key).let { null }
        } catch (e: Exception) {
            logger.warn("Fikk feilmelding fra Valkey under forsøk på å hente verdi, returnerer null", e)
            null
        }

    override fun <K, V> update(key: K, value: V, ttl: Duration) {
        try {
            logger.info("Oppdaterer verdi for {}", key)
            jedisPool.resource.use {
                it.set(
                    objectMapper.writeValueAsString(key),
                    objectMapper.writeValueAsString(value),
                    SetParams().apply {
                        ex(ttl.inWholeSeconds)
                    },
                )
            }
        } catch (e: Exception) {
            logger.warn("Fikk feilmelding fra Valkey under forsøk på å oppdatere verdi", e)
            return
        }
    }

    private fun setupJedis(config: Map<String, String?>, instanceName: String): JedisPool {
        logger.info("Validerer config for $instanceName")
        config.entries.filter { it.key.contains("VALKEY_HOST") }.forEach { logger.info("${it.key}=${it.value}") }
        config.entries.filter { it.key.contains("VALKEY_PORT") }.forEach { logger.info("${it.key}=${it.value}") }
        val host = config["VALKEY_HOST_$instanceName"]!!
        val port = config["VALKEY_PORT_$instanceName"]!!.toInt()
        val username = config["VALKEY_USERNAME_$instanceName"]!!
        val password = config["VALKEY_PASSWORD_$instanceName"]!!
        val ssl = config["VALKEY_SSL_$instanceName"]?.toBoolean() ?: true

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

class InMemoryCache : Cache {
    private val objectMapper = databaseObjectMapper
    private val cache = ConcurrentHashMap<String, String>()

    override fun <K, V> get(key: K, clazz: Class<V>) =
        cache[objectMapper.writeValueAsString(key)]?.let { objectMapper.readValue(it, clazz) }

    override fun <K, V> update(key: K, value: V, ttl: Duration) {
        cache[objectMapper.writeValueAsString(key)] = objectMapper.writeValueAsString(value)
    }
}