package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

internal class BrevbakerPDF(private val pdfByggerService: PDFByggerService, private val pdfVedleggAppender: PDFVedleggAppender) {
    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev).let {
            pdfByggerService.producePDF(
                PDFRequest(
                    letterMarkup = it.letterMarkup,
                    attachments = it.attachments,
                    language = letter.language.toCode(),
                    brevtype = letter.template.letterMetadata.brevtype,
                    pdfVedlegg = mapPDFTitler(letter)
                )
            )
        }
            .let { pdfVedleggAppender.leggPaaVedlegg(
                it,
                letter.template.pdfAttachments.map { a -> a.doTransform(letter.toScope()) },
                letter.language.toCode())
            }
            .let { pdf ->
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

internal fun mapPDFTitler(letter: Letter<*>) =
    letter.template.pdfAttachments
        .map { it.data.eval(letter.toScope()) }
        .map { it.tittel(letter.language) }


@OptIn(InterneDataklasser::class)
private fun PDFVedleggData.tittel(language: Language) = this.let {
    PDFTittel(
        title = listOf(
            // TODO: Dette kjens litt hacky. Burde kunne finne på noko lurt med expression for å unngå dette
            LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                id = it.hashCode(),
                text = it.tittel[language.toCode()]!!,
            )
        ))
}