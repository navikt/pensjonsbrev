package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.config.ApplicationConfig
import io.valkey.ConnectionPoolConfig
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
    fun <K> hasValue(key: K): Boolean
    fun <K, T> get(key: K, clazz: Class<T>): T?
    fun <K, V> update(key: K, value: V, ttl: Duration = defaultTtl)
    suspend fun <K, V> cached(key: K, clazz: Class<V>, ttl: Duration = defaultTtl, fetch: suspend (K) -> V?): V?
}

class Valkey(config: ApplicationConfig, instanceName: String) : CacheImplementation {
    private val objectMapper = databaseObjectMapper

    private val jedisPool = setupJedis(config, instanceName)

    override suspend fun <K, V> cached(key: K, clazz: Class<V>, ttl: Duration, fetch: suspend (K) -> V?): V? = if (hasValue(key)) {
        get(key, clazz)
    } else {
        fetch(key)?.also { update(key, it, ttl) }
    }

    override fun <K> hasValue(key: K) = jedisPool.resource.use {
        it.get(objectMapper.write(key)) != null
    }

    override fun <K, T> get(key: K, clazz: Class<T>): T? =
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

    override fun <K> hasValue(key: K) = cache.get(objectMapper.write(key)) != null

    override fun <K, V> get(key: K, clazz: Class<V>) =
        cache.get(objectMapper.write(key))?.let { objectMapper.readValue(it, clazz) }

    override fun <K, V> update(key: K, value: V, ttl: Duration) {
        cache[objectMapper.write(key)] = objectMapper.write(value)
    }

    override suspend fun <K, V> cached(key: K, clazz: Class<V>, ttl: Duration, fetch: suspend (K) -> V?): V? = if (hasValue(key)) {
        get(key, clazz)
    } else {
        fetch(key)?.also { update(key, it, ttl) }
    }
}

private fun <V> ObjectMapper.write(value: V) = if (value is String) value else writeValueAsString(value)


private fun setupJedis(config: ApplicationConfig, instanceName: String): JedisPool {
    val host = config.getString("VALKEY_HOST_$instanceName")
    val port = config.getString("VALKEY_PORT_$instanceName").toInt()
    val username = config.getString("VALKEY_USERNAME_$instanceName")
    val password = config.getString("VALKEY_PASSWORD_$instanceName")

    val config = ConnectionPoolConfig()

    // It is recommended that you set maxTotal = maxIdle = 2*minIdle for best performance
    // In cluster mode, please note that each business machine will contain up to maxTotal links,
    // and the total number of connections = maxTotal * number of machines
    config.maxTotal = 32
    config.maxIdle = 32
    config.minIdle = 16

    return JedisPool(
        HostAndPort(host, port),
        DefaultJedisClientConfig.builder()
            .ssl(true)
            .user(username)
            .password(password)
            .build()
    )
}

private fun ApplicationConfig.getString(key: String) =
    requireNotNull(propertyOrNull(key)?.getString()) { "Missing required config key: $key" }