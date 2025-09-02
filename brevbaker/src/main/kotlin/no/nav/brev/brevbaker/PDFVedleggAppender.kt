package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedleggData>,
        spraak: LanguageCode,
    ): PDFCompilationOutput
}