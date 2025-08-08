package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
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
    val data: BrevbakerBrevdata,
    val sider: Map<Side, Map<String, String?>>
) {
    @JvmInline
    value class Side(val sidenummer: Int)

    init {
        require(sider.keys.size == sider.keys.map { it.sidenummer }.distinct().size)
        require(sider.keys.minOf { it.sidenummer } == 1)
        require(sider.keys.maxOf { it.sidenummer } == sider.keys.size + 1)
    }
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.type == other.type && this.data == other.data && this.sider == other.sider
    }
    override fun hashCode() = Objects.hash(type, data, sider)
    override fun toString() = "PDFVedlegg(type=$type, data=$data, sider=$sider)"
}