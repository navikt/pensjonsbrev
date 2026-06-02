package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<out T : BrevTemplate<AutobrevData, Brevkode.Automatisk>>(
    override val name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    ) : TemplateResource<Brevkode.Automatisk, T, BestillBrevRequest<Brevkode.Automatisk>>, TemplateLibrary<Brevkode.Automatisk, T> by TemplateLibraryImpl(templates) {
    private val brevbaker = Brevbaker(pdfByggerService, PDFVedleggAppenderImpl)
    private val letterFactory: LetterFactory<Brevkode.Automatisk> = LetterFactory(emptySet())

    override fun kodeOf(key: String): Brevkode.Automatisk = AutomatiskBrevkode(key)

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Brevkode.Automatisk>): LetterResponse =
        brevbaker.renderPDF(createLetter(brevbestilling))

    override fun renderHTML(brevbestilling: BestillBrevRequest<Brevkode.Automatisk>): LetterResponse =
        brevbaker.renderHTML(createLetter(brevbestilling))

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Brevkode.Automatisk>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    private fun createLetter(brevbestilling: BestillBrevRequest<Brevkode.Automatisk>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

}
