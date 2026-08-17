package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupLetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<AutobrevData, Kode>>(
    override val name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    ) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>, TemplateLibrary<Kode, T> by TemplateLibraryImpl(templates) {
    private val brevbaker = Brevbaker(pdfByggerService)
    private val letterFactory: LetterFactory<Kode> = LetterFactory(emptySet())

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderPDF(createLetter(brevbestilling), brevbestilling.pdfVedlegg)

    suspend fun renderPDFV2(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderPDFV2(createLetter(brevbestilling), brevbestilling.pdfVedlegg)

    override fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbaker.renderHTML(createLetter(brevbestilling))

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    fun renderLetterMarkupV2(brevbestilling: BestillBrevRequest<Kode>): MarkupLetterMarkup =
        brevbaker.renderLetterMarkupV2(createLetter(brevbestilling))

    private fun createLetter(brevbestilling: BestillBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

}
