package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class Brevbaker(pdfByggerService: PDFByggerService) {
    private val brevbakerPDF = BrevbakerPDF(pdfByggerService)

    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        brevbakerPDF.renderPDF(letter, null)

    suspend fun renderRedigertBrevPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup) =
        brevbakerPDF.renderPDF(letter, redigertBrev)

    fun renderHTML(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        BrevbakerHTML.renderHTML(letter, null)

    fun renderRedigertBrevHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup): LetterResponse =
        BrevbakerHTML.renderHTML(letter, redigertBrev)

    fun renderJSON(letter: Letter<BrevbakerBrevdata>): LetterMarkup = BrevbakerJSON.renderJSON(letter)
}