package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Side
import no.nav.pensjon.brevbaker.api.model.VedleggType
import java.util.Objects


class PDFVedlegg(val type: VedleggType, val sider: List<Side>) {
    init {
        require(sider.size == sider.map { it.sidenummer }.distinct().size)
        require(sider.isEmpty() || sider.minOf { it.sidenummer } == 1)
        require(sider.isEmpty() || sider.maxOf { it.sidenummer } == sider.size)
    }
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.type == other.type && this.sider == other.sider
    }
    override fun hashCode() = Objects.hash(type, sider)
    override fun toString() = "PDFVedlegg(type=$type, sider=$sider)"
}