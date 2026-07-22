package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brev.skribenten.foerstesidegenerator.PDFMerger
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray
}

class PDFVedleggAppenderImpl : PDFVedleggAppender {
    override fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray = PDFMerger.merge(pdfCompilationOutput, attachments.map { VedleggAppender.lesInnVedlegg(it, spraak) })
}
