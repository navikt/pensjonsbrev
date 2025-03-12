package no.nav.pensjon.brevbaker.api.model

enum class PDFVedleggType {
    P1
}

data class PDFVedlegg(
    val type: PDFVedleggType,
    val data: Map<String, Any>
)