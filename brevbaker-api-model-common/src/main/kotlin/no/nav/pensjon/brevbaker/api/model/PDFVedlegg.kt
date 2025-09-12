package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.brevbaker.vedlegg.PDFVedlegg

interface PDFVedleggData {
    val tittel: Map<LanguageCode, String>
    fun somPDFVedlegg(): PDFVedlegg
}