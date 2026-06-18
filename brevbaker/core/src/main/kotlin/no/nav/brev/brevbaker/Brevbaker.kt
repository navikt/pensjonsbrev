package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class Brevbaker(
    pdfByggerService: PDFByggerService,
    pdfVedleggAppender: PDFVedleggAppender,
) {
    private val brevbakerPDF = BrevbakerPDF(pdfByggerService, pdfVedleggAppender)

    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        brevbakerPDF.renderPDF(letter, null)

    suspend fun renderRedigertBrevPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup, redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap()) =
        brevbakerPDF.renderPDF(letter, redigertBrev, redigerteVedlegg)

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
}