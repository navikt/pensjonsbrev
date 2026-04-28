package no.nav.pensjon.brevbaker.api.model

interface AlltidValgbartVedleggKode {
    val kode: String
    val visningstekst: String
    val spraak: Set<LanguageCode>
}

// TODO: Fjern default-verdi for spraak etter første deploy
class AlltidValgbartVedleggBrevkode(override val kode: String, override val visningstekst: String, override val spraak: Set<LanguageCode> = setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH)): AlltidValgbartVedleggKode {
    init {
        require(kode.length <= 50)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AlltidValgbartVedleggKode) return false
        return kode == other.kode
    }
    override fun hashCode(): Int = kode.hashCode()
    override fun toString(): String = "AlltidValgbartVedleggKode(kode=$kode, visningstekst=$visningstekst, spraak=$spraak)"
}