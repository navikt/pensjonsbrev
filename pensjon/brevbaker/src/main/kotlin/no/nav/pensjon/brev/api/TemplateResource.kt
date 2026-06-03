package no.nav.pensjon.brev.api

import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

interface TemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>, Request : BrevRequest<Kode>> {

    val name: String

    suspend fun renderPDF(brevbestilling: Request): LetterResponse

    fun renderHTML(brevbestilling: Request): LetterResponse

    fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup

    fun listTemplatesWithMetadata(): List<TemplateDescription>
    fun listTemplatekeys(): Set<String>

    fun getTemplate(kode: Kode): BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?
}

fun countLetter(brevkode: Brevkode<*>): Unit =
    Metrics.prometheusRegistry.counter(
        "pensjon_brevbaker_letter_request_count",
        listOf(Tag.of("brevkode", brevkode.kode()))
    ).increment()