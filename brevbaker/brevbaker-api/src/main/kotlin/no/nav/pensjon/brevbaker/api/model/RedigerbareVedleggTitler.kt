package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import java.util.Objects

class RedigerbareVedleggTitler(val vedlegg: List<Vedlegg>) {
    override fun equals(other: Any?) = other is RedigerbareVedleggTitler && vedlegg == other.vedlegg
    override fun hashCode() = vedlegg.hashCode()
    override fun toString(): String = "RedigerbareVedleggTitler(vedlegg=$vedlegg)"

    class Vedlegg(val id: VedleggId, val tittel: String) {
        override fun equals(other: Any?) = other is Vedlegg && id == other.id && tittel == other.tittel
        override fun hashCode() = Objects.hash(id, tittel)
        override fun toString(): String = "Vedlegg(id=$id, tittel=$tittel)"
    }
}
