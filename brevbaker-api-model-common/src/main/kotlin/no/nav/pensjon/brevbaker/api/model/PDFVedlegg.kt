package no.nav.pensjon.brevbaker.api.model

interface PDFVedleggData {
    val name: String
    val tittel: Map<LanguageCode, String>
}

class EmptyPDFVedleggData(override val name: String, override val tittel: Map<LanguageCode, String>) : PDFVedleggData