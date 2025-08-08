package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.util.Objects

class VedleggType(
    val name: String,
    val tittel: String
) {
    override fun equals(other: Any?): Boolean {
        if (other !is VedleggType) { return false}
        return name == other.name && tittel == other.tittel
    }
    override fun hashCode() = Objects.hash(name, tittel)
    override fun toString() = "VedleggType(name='$name', tittel='$tittel')"
}

class PDFVedlegg(
    val type: VedleggType,
    val data: BrevbakerBrevdata
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.type == other.type && this.data == other.data
    }
    override fun hashCode() = Objects.hash(type, data)
    override fun toString() = "PDFVedlegg(type=$type, data=$data)"
}