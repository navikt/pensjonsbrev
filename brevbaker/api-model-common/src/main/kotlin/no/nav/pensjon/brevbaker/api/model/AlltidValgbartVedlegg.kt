package no.nav.pensjon.brevbaker.api.model

interface AlltidValgbartVedleggKode {
    fun kode(): String
    fun visningstekst(): String
}

class AlltidValgbartVedleggBrevkode(private val kode: String, private val visningstekst: String): AlltidValgbartVedleggKode {
    init {
        require(kode.length <= 50)
    }
    override fun kode(): String = kode
    override fun visningstekst(): String = visningstekst

    override fun equals(other: Any?): Boolean {
        if (other !is AlltidValgbartVedleggKode) return false
        return kode == other.kode()
    }
    override fun hashCode(): Int = super.hashCode()
    override fun toString(): String = "AlltidValgbartVedleggKode(kode=$kode, visningstekst=$visningstekst)"
}