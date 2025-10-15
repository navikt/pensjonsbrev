package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import io.valkey.DefaultJedisClientConfig
import io.valkey.HostAndPort
import io.valkey.JedisPool
import io.valkey.params.SetParams
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val defaultTtl = 30.minutes

interface CacheImplementation {
    fun <K, V> get(key: K, clazz: Class<V>): V?
    fun <K, V> update(key: K, value: V, ttl: Duration = defaultTtl)
    suspend fun <K, V> cached(key: K, clazz: Class<V>, ttl: Duration = defaultTtl, fetch: suspend (K) -> V?): V? =
        get(key, clazz) ?: fetch(key)?.also { update(key, it, ttl) }
}

class Valkey(config: Map<String, String?>, instanceName: String) : CacheImplementation {
    private val objectMapper = databaseObjectMapper

    private val jedisPool = setupJedis(config, instanceName)

    override fun <K, V> get(key: K, clazz: Class<V>): V? =
        jedisPool.resource.use { it.get(objectMapper.write(key))?.let { k -> objectMapper.readValue(k, clazz) } }

    override fun <K, V> update(key: K, value: V, ttl: Duration) {
        jedisPool.resource.use {
            it.set(
                objectMapper.write(key),
                objectMapper.write(value),
                SetParams().apply {
                    ex(ttl.inWholeSeconds)
                    nx()
                },
            )
        }
    }
}

class InMemoryCache : CacheImplementation {
    private val objectMapper = databaseObjectMapper
    private val cache = ConcurrentHashMap<String, String>()

    override fun <K, V> get(key: K, clazz: Class<V>) =
        cache[objectMapper.write(key)]?.let { objectMapper.readValue(it, clazz) }

    override fun <K, V> update(key: K, value: V, ttl: Duration) {
        cache[objectMapper.write(key)] = objectMapper.write(value)
    }
}

private fun <V> ObjectMapper.write(value: V) = if (value is String) value else writeValueAsString(value)


private fun setupJedis(config: Map<String, String?>, instanceName: String): JedisPool {
    val host = config["VALKEY_HOST_$instanceName"]!!
    val port = config["VALKEY_PORT_$instanceName"]!!.toInt()
    val username = config["VALKEY_USERNAME_$instanceName"]!!
    val password = config["VALKEY_PASSWORD_$instanceName"]!!

    return JedisPool(
        HostAndPort(host, port),
        DefaultJedisClientConfig.builder()
            .ssl(true)
            .user(username)
            .password(password)
            .build()
    )
}