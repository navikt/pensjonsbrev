package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.VedleggType
import java.util.Objects


class PDFVedlegg(val name: String, val type: VedleggType, val sider: List<Side>) {
    init {
        require(sider.size == sider.map { it.sidenummer }.distinct().size)
        require(sider.isEmpty() || sider.minOf { it.sidenummer } == 1)
        require(sider.isEmpty() || sider.maxOf { it.sidenummer } == sider.size)
    }
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.name == other.name && this.type == other.type && this.sider == other.sider
    }
    override fun hashCode() = Objects.hash(name, type, sider)
    override fun toString() = "PDFVedlegg(name=$name, type=$type, sider=$sider)"
}

class Side(val sidenummer: Int, val originalSide: Int, val felt: Map<String, String?>) {
    override fun equals(other: Any?): Boolean {
        if (other !is Side) { return false}
        return sidenummer == other.sidenummer && originalSide == other.originalSide
    }
    override fun hashCode() = Objects.hash(sidenummer, originalSide)
    override fun toString() = "Side(sidenummer=$sidenummer, originalSide=$originalSide)"
}