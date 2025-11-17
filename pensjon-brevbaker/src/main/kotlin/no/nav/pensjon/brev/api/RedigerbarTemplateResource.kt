package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brevbaker.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class RedigerbarTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
) : TemplateResource<Kode, T, BestillRedigertBrevRequest<Kode>>(name, templates, pdfByggerService) {

    fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            brevbaker.renderLetterMarkup(createLetter(kode, letterData, language, felles))
        }

    fun renderLetterMarkupWithDataUsage(brevbestilling: BestillBrevRequest<Kode>): LetterMarkupWithDataUsage =
        with(brevbestilling) {
            brevbaker.renderLetterMarkupWithDataUsage(createLetter(kode, letterData, language, felles))
        }

    override suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderRedigertBrevPDF(createLetter(kode, letterData, language, felles), letterMarkup)
        }

    override fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderRedigertBrevHTML(createLetter(kode, letterData, language, felles), letterMarkup)
        }
}