package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedleggData>,
        spraak: Language,
    ): PDFCompilationOutput
}