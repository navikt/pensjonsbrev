package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class Brevbaker(
    pdfByggerService: PDFByggerService,
    pdfVedleggAppender: PDFVedleggAppender,
    typstToggleAuto: FeatureToggle? = null,
    typstToggleRedigerbar: FeatureToggle? = null,
) {
    private val brevbakerPDF = BrevbakerPDF(pdfByggerService, pdfVedleggAppender, typstToggleAuto, typstToggleRedigerbar)

    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        brevbakerPDF.renderPDF(letter, null)

    suspend fun renderRedigertBrevPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup) =
        brevbakerPDF.renderPDF(letter, redigertBrev)

    fun renderHTML(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        BrevbakerHTML.renderHTML(letter, null)

    fun renderRedigertBrevHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup): LetterResponse =
        BrevbakerHTML.renderHTML(letter, redigertBrev)

    fun <T: BrevbakerBrevdata> renderLetterMarkup(letter: Letter<T>): LetterMarkup =
        BrevbakerLetterMarkup.renderLetterMarkup(letter)

    fun <T: BrevbakerBrevdata> renderLetterMarkupWithDataUsage(letter: Letter<T>): LetterMarkupWithDataUsage =
        BrevbakerLetterMarkup.renderLetterMarkupWithDataUsage(letter)
}