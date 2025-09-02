package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

class VedleggType(
    val name: String,
    val tittel: Map<LanguageCode, String>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is VedleggType) { return false}
        return name == other.name && tittel == other.tittel
    }
    override fun hashCode() = Objects.hash(name, tittel)
    override fun toString() = "VedleggType(name='$name', tittel='$tittel')"
}

interface PDFVedleggData {
    val tittel: VedleggType
}

class EmptyPDFVedleggData(override val tittel: VedleggType) : PDFVedleggData