package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.ToggleName

interface Brevkode<T: Brevkode<T>> {

    interface Automatisk : Brevkode<Automatisk>

    interface Redigerbart : Brevkode<Redigerbart>

    fun kode(): String

    fun toggle(): ToggleName? = null
}

@JvmInline
value class RedigerbarBrevkode(private val kode: String) : Brevkode.Redigerbart {
    init {
        require(kode.length <= 50)
    }
    override fun kode(): String = kode
}

@JvmInline
value class AutomatiskBrevkode(private val kode: String): Brevkode.Automatisk {
    init {
        require(kode.length <= 50)
    }
    override fun kode(): String = kode
}