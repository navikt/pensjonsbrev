package no.nav.pensjon.brev.api

import com.fasterxml.jackson.core.JacksonException
import io.ktor.features.*
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

class LetterResource(val templateResource: TemplateResource = TemplateResource()) {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter<*> {
        // TODO: Should probably be 400 exception since it is a reference in the body that dont exists
        val template: LetterTemplate<*, *> = templateResource.getTemplate(letterRequest.template)
            ?: throw NotFoundException("Template '${letterRequest.template}' doesn't exist")

        val language = letterRequest.language.toLanguage()
        if (!template.language.supports(language)) {
            // TODO: Should be 400 response
            throw IllegalArgumentException("Template '${template.name}' doesn't support language: ${letterRequest.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(letterRequest, template),
            language = language,
            felles = letterRequest.felles
        )
    }

    private fun parseArgument(letterRequest: LetterRequest, template: LetterTemplate<*, *>): Any =
        try {
            objectMapper.convertValue(letterRequest.letterData, template.letterDataType.java)
        } catch (e: JacksonException) {
            // TODO: Should be 400 response
            throw IllegalArgumentException("Could not parse letterData", e)
        }

}
