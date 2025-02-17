package no.nav.brev.brevbaker

import io.ktor.http.ContentType
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import java.util.Base64

private val base64Decoder = Base64.getDecoder()

class BrevbakerPDF(private val pdfByggerService: PDFByggerService) {
    suspend fun lagPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev)
            .let { pdfByggerService.producePDF(
                PDFRequest(
                letterMarkup = it.letterMarkup,
                attachments = it.attachments,
                language = letter.language.toCode(),
                felles = letter.felles,
                brevtype = letter.template.letterMetadata.brevtype
            )
            ) }
            .let { pdf ->
                LetterResponse(
                    file = base64Decoder.decode(pdf.base64PDF),
                    contentType = ContentType.Application.Pdf.toString(),
                    letterMetadata = letter.template.letterMetadata
                )
            }

    private fun renderCompleteMarkup(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterWithAttachmentsMarkup =
        letter.toScope().let { scope ->
            LetterWithAttachmentsMarkup(
                redigertBrev ?: Letter2Markup.renderLetterOnly(scope, letter.template),
                Letter2Markup.renderAttachmentsOnly(scope, letter.template),
            )
        }
}