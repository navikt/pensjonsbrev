package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.Objects


class PDFVedlegg(val filnavn: String, val tittel: Map<LanguageCode, String>, val sider: List<Side>) {
    init {
        require(sider.size == sider.map { it.sidenummer }.distinct().size)
        require(sider.isEmpty() || sider.minOf { it.sidenummer } == 1)
        require(sider.isEmpty() || sider.maxOf { it.sidenummer } == sider.size)
    }
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.filnavn == other.filnavn && this.tittel == other.tittel && this.sider == other.sider
    }
    override fun hashCode() = Objects.hash(filnavn, tittel, sider)
    override fun toString() = "PDFVedlegg(name=$filnavn, tittel=$tittel, sider=$sider)"
}

class Side(val sidenummer: Int, val filnavn: String, val felt: Map<String, String?>) {
    override fun equals(other: Any?): Boolean {
        if (other !is Side) { return false}
        return sidenummer == other.sidenummer && filnavn == other.filnavn && felt == other.felt
    }
    override fun hashCode() = Objects.hash(sidenummer, filnavn, felt)
    override fun toString() = "Side(sidenummer=$sidenummer, filnavn=$filnavn, felt=$felt)"
}