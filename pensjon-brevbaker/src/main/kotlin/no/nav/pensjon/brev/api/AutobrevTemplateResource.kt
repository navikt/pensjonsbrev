package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<AutobrevData, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    ) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>(name) {
    private val brevbaker = Brevbaker(pdfByggerService, PDFVedleggAppenderImpl)
    private val templateLibrary: TemplateLibrary<Kode, T> = TemplateLibrary(templates)
    private val letterFactory: LetterFactory<Kode> = LetterFactory()

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderPDF(createLetter(brevbestilling))

    override fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderHTML(createLetter(brevbestilling))

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    override fun listTemplatesWithMetadata() = templateLibrary.listTemplatesWithMetadata()
    override fun listTemplatekeys() = templateLibrary.listTemplatekeys()

    override fun getTemplate(kode: Kode) = templateLibrary.getTemplate(kode)

    private fun createLetter(brevbestilling: BestillBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

}
