package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillAutobrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<AutobrevData, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,

    ) : TemplateResource<Kode, T, BestillAutobrevRequest<Kode>>(name, templates, pdfByggerService) {

    override suspend fun renderPDF(brevbestilling: BestillAutobrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderPDF(createLetter(kode, letterData, language, felles))
        }

    override fun renderHTML(brevbestilling: BestillAutobrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderHTML(createLetter(kode, letterData, language, felles))
        }

    fun renderJSON(brevbestilling: BestillAutobrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            brevbaker.renderLetterMarkup(createLetter(kode, letterData, language, felles))
        }
}
