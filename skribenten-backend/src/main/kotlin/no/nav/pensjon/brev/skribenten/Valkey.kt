package no.nav.pensjon.brev.skribenten

import io.ktor.server.config.ApplicationConfig
import io.valkey.ConnectionPoolConfig
import io.valkey.DefaultJedisClientConfig
import io.valkey.HostAndPort
import io.valkey.JedisPool
import io.valkey.params.SetParams
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

interface CacheImplementation<K: Any, V: Any> {
    fun hasValue(key: K): Boolean
    fun update(key: K, value: V, sekunder: Int)
    fun delete(key: K)
}

class Valkey(config: ApplicationConfig, instanceName: String) : CacheImplementation<String, String> {
    companion object {
        private const val DEFAULT_TIMEOUT: Int = 2000
        private const val DEFAULT_REDIRECTIONS: Int = 5
    }

    private val jedisPool = setupJedis(config, instanceName)

    override fun hasValue(key: String) = jedisPool.resource.use {
        it.get(key) != null
    }

    override fun update(key: String, value: String, sekunder: Int) {
        jedisPool.resource.use {
            it.set(
                key,
                value,
                SetParams().apply {
                    ex(sekunder.toLong())
                    nx()
                },
            )
        }
    }

    override fun delete(key: String){
        jedisPool.resource.use { it.del(key) }
    }
}

class InMemoryCache<K: Any, V: Any> : CacheImplementation<K, V> {
    private val cache = ConcurrentHashMap<K, V>()

    override fun hasValue(key: K) = cache.get(key) != null

    override fun update(key: K, value: V, sekunder: Int) {
        cache[key] = value
    }

    override fun delete(key: K) {
        cache.remove(key)
    }
}


private fun setupJedis(config: ApplicationConfig, instanceName: String): JedisPool {
    val uri = URI(config.getString("VALKEY_URI_$instanceName"))
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