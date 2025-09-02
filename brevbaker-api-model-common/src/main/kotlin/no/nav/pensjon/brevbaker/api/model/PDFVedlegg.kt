package no.nav.pensjon.brevbaker.api.model

interface PDFVedleggData {
    val name: String
    val tittel: Map<LanguageCode, String>
}