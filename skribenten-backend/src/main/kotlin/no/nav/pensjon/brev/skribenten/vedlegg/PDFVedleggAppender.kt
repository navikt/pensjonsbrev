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
    ): ByteArray {
        /* Ikke strengt nødvendig å returnere her, det vil fungere uten, men optimalisering.
        De aller, aller fleste brevene har ikke PDF-vedlegg, så de trenger ikke gå gjennom denne løypa
         */
        if (attachments.isEmpty()) {
            return pdfCompilationOutput
        }

        return PDFMerger.mergePDFs(pdfCompilationOutput, attachments.map { VedleggAppender.lesInnVedlegg(it, spraak) })
    }
}
