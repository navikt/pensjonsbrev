package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.micrometer.core.instrument.Tag
import no.nav.brev.brevbaker.Brevbaker
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode

private val objectMapper = brevbakerJacksonObjectMapper()

abstract class TemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>, Request : BrevRequest<Kode>>(
    val name: String,
    templates: Set<T>,
    laTeXCompilerService: LaTeXCompilerService,
) {
    abstract suspend fun renderPDF(brevbestilling: Request): LetterResponse

    abstract fun renderHTML(brevbestilling: Request): LetterResponse

    protected val brevbaker = Brevbaker(laTeXCompilerService)
    private val templateLibrary: TemplateLibrary<Kode, T> = TemplateLibrary(templates)

    fun listTemplatesWithMetadata() = templateLibrary.listTemplatesWithMetadata()
    fun listTemplatekeys() = templateLibrary.listTemplatekeys()

    fun getTemplate(kode: Kode) = templateLibrary.getTemplate(kode)

    fun countLetter(brevkode: Kode): Unit =
        Metrics.prometheusRegistry.counter(
            "pensjon_brevbaker_letter_request_count",
            listOf(Tag.of("brevkode", brevkode.kode()))
        ).increment()

    protected fun createLetter(
        brevkode: Kode,
        brevdata: BrevbakerBrevdata,
        spraak: LanguageCode,
        felles: Felles
    ,): Letter<BrevbakerBrevdata> {
        val template =
            getTemplate(brevkode)?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

        val language = spraak.toLanguage()
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${brevkode}' doesn't support language: ${template.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(brevdata, template),
            language = language,
            felles = felles,
        )
    }

    private fun parseArgument(
        letterData: BrevbakerBrevdata,
        template: LetterTemplate<*, BrevbakerBrevdata>,
    ): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}