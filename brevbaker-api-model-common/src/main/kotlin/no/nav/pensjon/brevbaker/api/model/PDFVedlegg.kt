package no.nav.pensjon.brevbaker.api.model

interface PDFVedleggData {
    val filnavn: String
    val tittel: Map<LanguageCode, String>
}