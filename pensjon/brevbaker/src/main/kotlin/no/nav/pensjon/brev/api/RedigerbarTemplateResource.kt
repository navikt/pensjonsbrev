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
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class RedigerbarTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    override val name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    val alltidValgbareVedlegg: Set<AlltidValgbartVedlegg<*>>,
) : TemplateResource<Kode, T, BestillRedigertBrevRequest<Kode>>, TemplateLibrary<Kode, T> by TemplateLibraryImpl(templates) {
    private val brevbaker = Brevbaker(pdfByggerService, PDFVedleggAppenderImpl)
    private val letterFactory: LetterFactory<Kode> = LetterFactory(alltidValgbareVedlegg)

    override fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        brevbaker.renderLetterMarkup(createLetter(brevbestilling))

    fun renderLetterMarkupWithDataUsage(brevbestilling: BestillBrevRequest<Kode>): LetterMarkupWithDataUsage =
        brevbaker.renderLetterMarkupWithDataUsage(createLetter(brevbestilling))

    fun renderRedigerbartVedleggMarkupTitler(brevbestilling: BestillBrevRequest<Kode>): Map<VedleggId, List<LetterMarkup.ParagraphContent.Text>> =
        brevbaker.renderRedigerbartVedleggTitler(createLetter(brevbestilling))

    fun renderRedigerbartVedleggMarkup(brevbestilling: BestillBrevRequest<Kode>, vedleggId: String): LetterMarkup.Attachment? =
        brevbaker.renderRedigerbartVedleggMarkup(createLetter(brevbestilling), VedleggId(vedleggId))

    override suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        // TODO(redigerbart-vedlegg): fjern '?: emptyMap()' når redigerteVedlegg gjøres obligatorisk etter utrulling.
        brevbaker.renderRedigertBrevPDF(createLetter(brevbestilling), brevbestilling.letterMarkup, brevbestilling.redigerteVedlegg.tilVedleggIdMap())

    override fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        // TODO(redigerbart-vedlegg): fjern '?: emptyMap()' når redigerteVedlegg gjøres obligatorisk etter utrulling.
        brevbaker.renderRedigertBrevHTML(createLetter(brevbestilling), brevbestilling.letterMarkup, brevbestilling.redigerteVedlegg.tilVedleggIdMap())

    private fun Map<String, LetterMarkup.Attachment>?.tilVedleggIdMap(): Map<VedleggId, LetterMarkup.Attachment> =
        this?.mapKeys { VedleggId(it.key) } ?: emptyMap()

    private fun createLetter(brevbestilling: BestillBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

    private fun createLetter(brevbestilling: BestillRedigertBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))
}