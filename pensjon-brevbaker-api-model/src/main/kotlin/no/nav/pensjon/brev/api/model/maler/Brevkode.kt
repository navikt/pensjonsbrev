package no.nav.pensjon.brev.api.model.maler

interface Brevkode<T: Brevkode<T>> {

    interface Automatisk : Brevkode<Automatisk>

    interface Redigerbart : Brevkode<Redigerbart>

    fun kode(): String
}

@JvmInline
value class RedigerbarBrevkode(private val kode: String) : Brevkode.Redigerbart {
//    init {
//        require(kode.length <= 50)
//    }
    override fun kode(): String = kode
}

@JvmInline
value class AutomatiskBrevkode(private val kode: String): Brevkode.Automatisk {
//    init {
//        require(kode.length <= 50)
//    }
    override fun kode(): String = kode
}