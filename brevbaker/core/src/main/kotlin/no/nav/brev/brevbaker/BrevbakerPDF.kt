package no.nav.brev.brevbaker

import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkupV2
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestV2
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2

internal class BrevbakerPDF(
    private val pdfByggerService: PDFByggerService,
    private val pdfVedleggAppender: PDFVedleggAppender,
) {
    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null, redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap()): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev, redigerteVedlegg).let { markup ->
            pdfByggerService.producePDF(
                PDFRequest(
                    letterMarkup = markup.letterMarkup,
                    attachments = markup.attachments,
                    language = letter.language.toCode(),
                    brevtype = letter.template.letterMetadata.brevtype,
                    pdfVedlegg = Letter2Markup.renderPDFTitlesOnly(letter.toScope(), letter.template)
                ),
            )
        }.let { pdf ->
            pdfVedleggAppender.leggPaaVedlegg(
                pdf,
                letter.template.pdfAttachments
                    .filter { a -> a.predicate.eval(letter.toScope()) }
                    .map { a -> a.eval(letter.toScope()) },
                letter.language.toCode()
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
        redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap(),
    ): LetterWithAttachmentsMarkup = letter.toScope().let { scope ->
        LetterWithAttachmentsMarkup(
            redigertBrev ?: Letter2Markup.renderLetterOnly(scope, letter.template),
            Letter2Markup.renderAttachmentsOnly(scope, letter.template, redigerteVedlegg),
        )
    }

    suspend fun renderPDFV2(
        letter: Letter<BrevbakerBrevdata>,
        redigertBrev: LetterMarkupV2? = null,
        redigerteVedlegg: Map<VedleggId, LetterMarkupV2.Attachment> = emptyMap(),
    ): LetterResponse =
        renderCompleteMarkupV2(letter, redigertBrev, redigerteVedlegg).let { markup ->
            pdfByggerService.producePDFV2(
                PDFRequestV2(
                    letterMarkup = markup.letterMarkup,
                    attachments = markup.attachments,
                    language = letter.language.toCode(),
                    brevtype = letter.template.letterMetadata.brevtype,
                    pdfVedlegg = Letter2MarkupV2.renderPDFTitlesOnly(letter.toScope(), letter.template)
                ),
            )
        }.let { pdf ->
            pdfVedleggAppender.leggPaaVedlegg(
                pdf,
                letter.template.pdfAttachments
                    .filter { a -> a.predicate.eval(letter.toScope()) }
                    .map { a -> a.eval(letter.toScope()) },
                letter.language.toCode()
            )
        }.let { pdf ->
            LetterResponse(
                file = pdf.bytes,
                contentType = ContentTypes.PDF,
                letterMetadata = letter.template.letterMetadata
            )
        }

    private fun renderCompleteMarkupV2(
        letter: Letter<BrevbakerBrevdata>,
        redigertBrev: LetterMarkupV2? = null,
        redigerteVedlegg: Map<VedleggId, LetterMarkupV2.Attachment> = emptyMap(),
    ): LetterWithAttachmentsMarkupV2 = letter.toScope().let { scope ->
        LetterWithAttachmentsMarkupV2(
            redigertBrev ?: Letter2MarkupV2.renderLetterOnly(scope, letter.template),
            Letter2MarkupV2.renderAttachmentsOnly(scope, letter.template, redigerteVedlegg),
        )
    }
}