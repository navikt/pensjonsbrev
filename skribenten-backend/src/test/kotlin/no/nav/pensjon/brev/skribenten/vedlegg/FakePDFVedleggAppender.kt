package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

class FakePDFVedleggAppender : PDFVedleggAppender {
    override fun leggPaaVedlegg(pdfCompilationOutput: PDFCompilationOutput, attachments: List<PDFVedlegg>, spraak: LanguageCode): PDFCompilationOutput = pdfCompilationOutput
}