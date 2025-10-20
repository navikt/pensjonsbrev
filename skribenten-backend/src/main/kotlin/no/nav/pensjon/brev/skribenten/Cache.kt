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
import kotlin.time.Duration.Companion.minutes

private val defaultTtl = 10.minutes

interface Cache {
    fun <K, V> get(prefix: String, key: K, clazz: Class<V>): V?
    fun <K, V> update(prefix: String, key: K, value: V, ttl: Duration = defaultTtl)
    suspend fun <K, V> cached(prefix: String, key: K, clazz: Class<V>, ttl: Duration = defaultTtl, fetch: suspend (K) -> V?): V? =
        get(prefix, key, clazz) ?: fetch(key)?.also { update(prefix, key, it, ttl) }
}

class Valkey(
    config: Map<String, String?>,
    instanceName: String,
    private val objectMapper: ObjectMapper = databaseObjectMapper,
) : Cache {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val jedisPool = setupJedis(config, instanceName.uppercase())

    override fun <K, V> get(prefix: String, key: K, clazz: Class<V>): V? =
        try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3) { it.get(objectMapper.writeWithPrefix(prefix, key)) }
                    ?.let { k -> objectMapper.readValue(k, clazz) }
            }
        } catch (e: Exception) {
            logger.warn("Fikk feilmelding fra Valkey under forsøk på å hente verdi, returnerer null", e)
            null
        }

    override fun <K, V> update(prefix: String, key: K, value: V, ttl: Duration) {
        try {
            jedisPool.resource.use {
                retryOgPakkUt(times = 3) {
                    it.set(
                        objectMapper.writeWithPrefix(prefix, key),
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

    private fun <T> ObjectMapper.writeWithPrefix(prefix: String, key: T): String = writeValueAsString(prefix + "-" + writeValueAsString(key))

    private fun setupJedis(config: Map<String, String?>, instanceName: String): JedisPool {
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

    override fun <K, V> get(prefix: String, key: K, clazz: Class<V>) =
        cache[prefix + objectMapper.writeValueAsString(key)]?.let { objectMapper.readValue(it, clazz) }

    override fun <K, V> update(prefix: String, key: K, value: V, ttl: Duration) {
        cache[prefix + objectMapper.writeValueAsString(key)] = objectMapper.writeValueAsString(value)
    }
}