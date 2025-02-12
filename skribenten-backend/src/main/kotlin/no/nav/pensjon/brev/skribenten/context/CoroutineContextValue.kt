package no.nav.pensjon.brev.skribenten.context

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

interface ContextValue<V> {
    suspend fun get(): V?

    suspend fun require(): V
}

class ContextValueProvider<V, E : CoroutineContext.Element, K : CoroutineContext.Key<E>>(
    private val key: K,
    private val name: String,
    private val value: E.() -> V,
) : ContextValue<V> {
    override suspend fun get(): V? {
        return coroutineContext[key]?.value()
    }

    override suspend fun require(): V {
        return get() ?: throw CoroutineContextValueException(name)
    }
}

class CoroutineContextValueException(keyName: String) : Exception("$keyName is not in CoroutineContext")
