package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class RedigerbarTemplateResource<out T : BrevTemplate<BrevbakerBrevdata, Brevkode.Redigerbart>>(
    override val name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    val alltidValgbareVedlegg: Set<AlltidValgbartVedlegg<*>>,
) : TemplateResource<Brevkode.Redigerbart, T, BestillRedigertBrevRequest<Brevkode.Redigerbart>>, TemplateLibrary<Brevkode.Redigerbart, T> by TemplateLibraryImpl(templates) {
    private val brevbaker = Brevbaker(pdfByggerService, PDFVedleggAppenderImpl)
    private val letterFactory: LetterFactory<Brevkode.Redigerbart> = LetterFactory(alltidValgbareVedlegg)

    override fun kodeOf(key: String): Brevkode.Redigerbart = RedigerbarBrevkode(key)

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Brevkode.Redigerbart>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    fun renderLetterMarkupWithDataUsage(brevbestilling: BestillBrevRequest<Brevkode.Redigerbart>): LetterMarkupWithDataUsage =
        brevbaker.renderLetterMarkupWithDataUsage(createLetter(brevbestilling))

    override suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Brevkode.Redigerbart>): LetterResponse =
        brevbaker.renderRedigertBrevPDF(createLetter(brevbestilling), brevbestilling.letterMarkup)

    override fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Brevkode.Redigerbart>): LetterResponse =
        brevbaker.renderRedigertBrevHTML(createLetter(brevbestilling), brevbestilling.letterMarkup)

    private fun createLetter(brevbestilling: BestillBrevRequest<Brevkode.Redigerbart>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

    private fun createLetter(brevbestilling: BestillRedigertBrevRequest<Brevkode.Redigerbart>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))
}