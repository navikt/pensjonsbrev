package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<AutobrevData, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    ) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>(name, templates, pdfByggerService) {

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderPDF(createLetter(brevbestilling))

    override fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderHTML(createLetter(brevbestilling))

    fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))
}
