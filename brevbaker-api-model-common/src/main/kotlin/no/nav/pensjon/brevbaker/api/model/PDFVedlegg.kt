package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

class VedleggType(
    val tittel: Map<LanguageCode, String>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is VedleggType) { return false}
        return tittel == other.tittel
    }
    override fun hashCode() = Objects.hash(tittel)
    override fun toString() = "VedleggType(tittel='$tittel')"
}

interface PDFVedleggData {
    val name: String
    val tittel: VedleggType
}

class EmptyPDFVedleggData(override val name: String, override val tittel: VedleggType) : PDFVedleggData