package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
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

    fun renderLetterMarkup(letter: Letter<BrevbakerBrevdata>): LetterMarkup =
        BrevbakerLetterMarkup.renderLetterMarkup(letter)

    fun renderLetterWithAttachmentsMarkup(letter: Letter<BrevbakerBrevdata>): LetterWithAttachmentsMarkup =
        BrevbakerLetterMarkup.renderLetterWithAttachmentsMarkup(letter)
}