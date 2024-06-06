package no.nav.pensjon.brev.api

import io.ktor.server.plugins.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.Felles

class ParseLetterDataException(msg: String, cause: Exception): Exception(msg, cause)

class LetterResource(val templateResource: TemplateResource = TemplateResource()) {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: AutobrevRequest): Letter<BrevbakerBrevdata> {
        val template: LetterTemplate<*, BrevbakerBrevdata> = templateResource.getAutoBrev(letterRequest.kode)
            ?: throw NotFoundException("Template '${letterRequest.kode}' doesn't exist")

        return create(template, letterRequest.language.toLanguage(), letterRequest.letterData, letterRequest.felles)
    }

    fun create(letterRequest: RedigerbartbrevRequest): Letter<BrevbakerBrevdata> {
        val template: LetterTemplate<*, BrevbakerBrevdata> = templateResource.getRedigerbartBrev(letterRequest.kode)
            ?: throw NotFoundException("Template '${letterRequest.kode}' doesn't exist")

        return create(template, letterRequest.language.toLanguage(), letterRequest.letterData, letterRequest.felles)
    }

    private fun create(template: LetterTemplate<*, BrevbakerBrevdata>, language: Language, letterData: BrevbakerBrevdata, felles: Felles): Letter<BrevbakerBrevdata> {
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${template.name}' doesn't support language: $language")
        }

        return Letter(
            template = template,
            argument = parseArgument(letterData, template),
            language = language,
            felles = felles,
        )
    }

    private fun parseArgument(letterData: Any, template: LetterTemplate<*, BrevbakerBrevdata>): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not parse letterData: ${e.message}", e)
        }

}
