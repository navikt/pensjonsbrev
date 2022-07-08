package no.nav.pensjon.brev.api

import io.ktor.server.plugins.*
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

class ParseLetterDataException(msg: String, cause: Exception): Exception(msg, cause)

class LetterResource(val templateResource: TemplateResource = TemplateResource()) {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter<*> {
        val template: LetterTemplate<*, *> = templateResource.getTemplate(letterRequest.template)
            ?: throw NotFoundException("Template '${letterRequest.template}' doesn't exist")

        val language = letterRequest.language.toLanguage()
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${template.name}' doesn't support language: ${letterRequest.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(letterRequest.letterData, template),
            language = language,
            felles = letterRequest.felles
        )
    }

    private fun parseArgument(letterData: Any, template: LetterTemplate<*, *>): Any =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not parse letterData: ${e.message}", e)
        }

}
