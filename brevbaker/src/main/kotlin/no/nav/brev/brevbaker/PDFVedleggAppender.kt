package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): PDFCompilationOutput
}