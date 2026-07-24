package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupLetterMarkup
import no.nav.brev.brevbaker.markup.Attachment as MarkupAttachment
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage as MarkupLetterMarkupWithDataUsage
import no.nav.brev.brevbaker.markup.outline.Text as MarkupText

class Brevbaker(
    pdfByggerService: PDFByggerService,
    pdfVedleggAppender: PDFVedleggAppender,
) {
    private val brevbakerPDF = BrevbakerPDF(pdfByggerService, pdfVedleggAppender)

    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        brevbakerPDF.renderPDF(letter, null)

    suspend fun renderPDFV2(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        brevbakerPDF.renderPDFV2(letter, null)

    suspend fun renderRedigertBrevPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup, redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap(), medPDFVedlegg: Boolean) =
        brevbakerPDF.renderPDF(letter, redigertBrev, redigerteVedlegg, medPDFVedlegg)

    suspend fun renderRedigertBrevV2PDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: MarkupLetterMarkup, redigerteVedlegg: Map<VedleggId, MarkupAttachment> = emptyMap()) =
        brevbakerPDF.renderPDFV2(letter, redigertBrev, redigerteVedlegg)

    fun renderHTML(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        BrevbakerHTML.renderHTML(letter, null)

    fun renderRedigertBrevHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup, redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap()): LetterResponse =
        BrevbakerHTML.renderHTML(letter, redigertBrev, redigerteVedlegg)

    fun <T: BrevbakerBrevdata> renderLetterMarkup(letter: Letter<T>): LetterMarkup =
        BrevbakerLetterMarkup.renderLetterMarkup(letter)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedleggTitler(letter: Letter<T>): Map<VedleggId, List<LetterMarkup.ParagraphContent.Text>> =
        BrevbakerLetterMarkup.renderRedigerbartVedleggTitler(letter)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedleggMarkup(letter: Letter<T>, vedleggId: VedleggId): LetterMarkup.Attachment? =
        BrevbakerLetterMarkup.renderRedigerbartVedlegg(letter, vedleggId)

    fun <T: BrevbakerBrevdata> renderLetterMarkupWithDataUsage(letter: Letter<T>): LetterMarkupWithDataUsage =
        BrevbakerLetterMarkup.renderLetterMarkupWithDataUsage(letter)

    fun <T: BrevbakerBrevdata> renderLetterMarkupV2(letter: Letter<T>): MarkupLetterMarkup =
        BrevbakerLetterMarkup.renderLetterMarkupV2(letter)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedleggV2Titler(letter: Letter<T>): Map<VedleggId, List<MarkupText>> =
        BrevbakerLetterMarkup.renderRedigerbartVedleggV2Titler(letter)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedleggV2Markup(letter: Letter<T>, vedleggId: VedleggId): MarkupAttachment? =
        BrevbakerLetterMarkup.renderRedigerbartVedleggV2(letter, vedleggId)

    fun <T: BrevbakerBrevdata> renderLetterMarkupWithDataUsageV2(letter: Letter<T>): MarkupLetterMarkupWithDataUsage =
        BrevbakerLetterMarkup.renderLetterMarkupWithDataUsageV2(letter)
}