package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

enum class PDFVedleggType(val tittel: String) {
    P1("P1 – Samlet melding om pensjonsvedtak"),
    InformasjonOmP1("Informasjon om skjemaet P1 og hvordan det brukes")
}

data class PDFVedlegg(
    val type: PDFVedleggType,
    val data: BrevbakerBrevdata
)