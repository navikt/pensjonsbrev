package no.nav.pensjon.etterlatte

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.maler.AllTemplates
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

class LetterResource(templates: AllTemplates) {
    private val objectMapper = jacksonObjectMapper()

    private val autoBrevMap: Map<Brevkode<*>, AutobrevTemplate<BrevbakerBrevdata>> =
        templates.hentAutobrevmaler().associateBy { it.kode }

    private fun getAutoBrev(kode: Brevkode<*>): LetterTemplate<*, *>? = autoBrevMap[kode]?.template

    fun create(letterRequest: BestillBrevRequest<*>): Letter<*> {
        val template: LetterTemplate<*, *> = getAutoBrev(letterRequest.kode)
            ?: throw NotFoundException("Template '${letterRequest.kode}' doesn't exist")

        val language = letterRequest.language.toLanguage()

        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${template.name}' doesn't support language: $language")
        }

        return Letter(
            template = template,
            argument = parseArgument(letterRequest.letterData, template),
            language = language,
            felles = letterRequest.felles,
        )
    }

    private fun parseArgument(letterData: BrevbakerBrevdata, template: LetterTemplate<*, *>): Any =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not parse letterData: ${e.message}", e)
        }

}
