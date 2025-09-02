package no.nav.pensjon.brevbaker.api.model

import java.util.Objects
import kotlin.collections.map

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

class Side(val sidenummer: Int, val originalSide: Int, val felt: Map<String, String?>) {
    override fun equals(other: Any?): Boolean {
        if (other !is Side) { return false}
        return sidenummer == other.sidenummer && originalSide == other.originalSide
    }
    override fun hashCode() = Objects.hash(sidenummer, originalSide)
    override fun toString() = "Side(sidenummer=$sidenummer, originalSide=$originalSide)"
}

interface PDFVedleggData {
    val tittel: VedleggType
}

class EmptyPDFVedleggData(override val tittel: VedleggType) : PDFVedleggData