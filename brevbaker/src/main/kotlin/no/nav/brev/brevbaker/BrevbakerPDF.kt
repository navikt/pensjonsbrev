package no.nav.brev.brevbaker

import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

internal class BrevbakerPDF(private val pdfByggerService: PDFByggerService) {
    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev).let {
            pdfByggerService.producePDF(
                PDFRequest(
                    letterMarkup = it.letterMarkup,
                    attachments = it.attachments,
                    language = letter.language.toCode(),
                    felles = letter.felles,
                    brevtype = letter.template.letterMetadata.brevtype,
                    pdfVedlegg = renderPDFAttachments(letter)
                )
            )
        }.let { pdf ->
            LetterResponse(
                file = pdf.bytes,
                contentType = ContentTypes.PDF,
                letterMetadata = letter.template.letterMetadata
            )
        }

    private fun renderCompleteMarkup(
        letter: Letter<BrevbakerBrevdata>,
        redigertBrev: LetterMarkup? = null,
    ): LetterWithAttachmentsMarkup = letter.toScope().let { scope ->
        LetterWithAttachmentsMarkup(
            redigertBrev ?: Letter2Markup.renderLetterOnly(scope, letter.template),
            Letter2Markup.renderAttachmentsOnly(scope, letter.template),
        )
    }
}

internal fun renderPDFAttachments(letter: Letter<*>) =
    letter.template.pdfAttachments
        .map { it.type to it.data.eval(letter.toScope()) }
        .map { PDFVedlegg(it.first, mapOf("data" to it.second)) }