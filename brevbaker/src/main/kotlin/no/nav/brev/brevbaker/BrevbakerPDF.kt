package no.nav.brev.brevbaker

import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

internal class BrevbakerPDF(private val pdfByggerService: PDFByggerService) {
    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev).let {
            pdfByggerService.producePDF(
                PDFRequest(
                    letterMarkup = it.letterMarkup,
                    attachments = it.attachments,
                    language = letter.language.toCode(),
                    brevtype = letter.template.letterMetadata.brevtype
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