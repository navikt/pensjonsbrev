package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class RedigerbarTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
) : TemplateResource<Kode, T, BestillRedigertBrevRequest<Kode>>(name, templates) {
    private val brevbaker = Brevbaker(pdfByggerService, PDFVedleggAppenderImpl)
    private val templateLibrary: TemplateLibrary<Kode, T> = TemplateLibrary(templates)

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    fun renderLetterMarkupWithDataUsage(brevbestilling: BestillBrevRequest<Kode>): LetterMarkupWithDataUsage =
        brevbaker.renderLetterMarkupWithDataUsage(createLetter(brevbestilling))

    override suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        brevbaker.renderRedigertBrevPDF(createLetter(brevbestilling), brevbestilling.letterMarkup)

    override fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        brevbaker.renderRedigertBrevHTML(createLetter(brevbestilling), brevbestilling.letterMarkup)

    override fun listTemplatesWithMetadata() = templateLibrary.listTemplatesWithMetadata()
    override fun listTemplatekeys() = templateLibrary.listTemplatekeys()
}