package no.nav.pensjon.brevbaker.api.model

interface AlltidValgbartVedleggKode {
    fun kode(): String
}

@JvmInline
value class AlltidValgbartVedleggBrevkode(private val kode: String): AlltidValgbartVedleggKode {
    init {
        require(kode.length <= 50)
    }
    override fun kode(): String = kode
}