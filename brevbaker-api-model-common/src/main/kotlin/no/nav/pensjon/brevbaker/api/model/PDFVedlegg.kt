package no.nav.pensjon.brevbaker.api.model

import java.util.Objects
import kotlin.collections.map

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
    val sider: Map<Side, Map<String, String?>>
) {

    init {
        require(sider.keys.size == sider.keys.map { it.sidenummer }.distinct().size)
        require(sider.keys.minOf { it.sidenummer } == 1)
        require(sider.keys.maxOf { it.sidenummer } == sider.keys.size + 1)
    }
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.type == other.type && this.sider == other.sider
    }
    override fun hashCode() = Objects.hash(type, sider)
    override fun toString() = "PDFVedlegg(type=$type, sider=$sider)"
}

class Side(val sidenummer: Int, val originalSide: Int = sidenummer) {
    override fun equals(other: Any?): Boolean {
        if (other !is Side) { return false}
        return sidenummer == other.sidenummer && originalSide == other.originalSide
    }
    override fun hashCode() = Objects.hash(sidenummer, originalSide)
    override fun toString() = "Side(sidenummer=$sidenummer, originalSide=$originalSide)"
}