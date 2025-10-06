package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): PDFCompilationOutput
}