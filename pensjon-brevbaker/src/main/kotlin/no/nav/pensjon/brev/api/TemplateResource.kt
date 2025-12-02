package no.nav.pensjon.brev.api

import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.BrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

abstract class TemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>, Request : BrevRequest<Kode>>(
    val name: String,
    templates: Set<T>,
) {
    abstract suspend fun renderPDF(brevbestilling: Request): LetterResponse

    abstract fun renderHTML(brevbestilling: Request): LetterResponse

    abstract fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup

    private val templateLibrary: TemplateLibrary<Kode, T> = TemplateLibrary(templates)
    private val letterFactory: LetterFactory<Kode> = LetterFactory()

    abstract fun listTemplatesWithMetadata(): List<TemplateDescription>
    abstract fun listTemplatekeys(): Set<String>

    fun getTemplate(kode: Kode) = templateLibrary.getTemplate(kode)

    protected fun createLetter(brevbestilling: BestillBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))

    protected fun createLetter(brevbestilling: BestillRedigertBrevRequest<Kode>) =
        letterFactory.createLetter(brevbestilling, getTemplate(brevbestilling.kode))
}

fun countLetter(brevkode: Brevkode<*>): Unit =
    Metrics.prometheusRegistry.counter(
        "pensjon_brevbaker_letter_request_count",
        listOf(Tag.of("brevkode", brevkode.kode()))
    ).increment()