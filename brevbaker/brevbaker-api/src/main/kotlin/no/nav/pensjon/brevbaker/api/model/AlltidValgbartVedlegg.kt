package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode
import java.util.Objects

class AlltidValgbartVedleggBrevkode(
    override val kode: String,
    override val visningstekst: String,
    override val spraak: Set<LanguageCode>,
    override val stoettedeBrevmaler: Collection<Brevkode.Redigerbart>,
) : AlltidValgbartVedleggKode {
    init {
        require(kode.length <= 50)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AlltidValgbartVedleggKode) return false
        return kode == other.kode && visningstekst == other.visningstekst && spraak == other.spraak && stoettedeBrevmaler.toSet() == other.stoettedeBrevmaler.toSet()
    }
    override fun hashCode(): Int = Objects.hash(kode, visningstekst, spraak, stoettedeBrevmaler)
    override fun toString(): String = "AlltidValgbartVedleggKode(kode=$kode, visningstekst=$visningstekst, spraak=$spraak, stoettedeBrevmaler=$stoettedeBrevmaler)"
}
