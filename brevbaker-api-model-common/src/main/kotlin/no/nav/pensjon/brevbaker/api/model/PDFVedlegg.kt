package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.util.Objects

enum class PDFVedleggType(val tittel: String) {
    P1("P1 – Samlet melding om pensjonsvedtak"),
    InformasjonOmP1("Informasjon om skjemaet P1 og hvordan det brukes")
}

class PDFVedlegg(
    val type: PDFVedleggType,
    val data: BrevbakerBrevdata
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return this.type == other.type && this.data == other.data
    }
    override fun hashCode() = Objects.hash(type, data)
    override fun toString() = "PDFVedlegg(type=$type, data=$data)"
}