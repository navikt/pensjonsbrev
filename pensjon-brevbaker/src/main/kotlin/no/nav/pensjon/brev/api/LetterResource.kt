package no.nav.pensjon.brev.api

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.node.ObjectNode
import io.ktor.features.*
import no.nav.pensjon.brev.api.dto.Felles
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

object LetterResource {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter<*> {
        val template: LetterTemplate<*, *> = TemplateResource.getTemplate(letterRequest.template)
            ?: throw NotFoundException("Template '${letterRequest.template}' doesn't exist")

        if (!template.language.supports(letterRequest.language)) {
            throw IllegalArgumentException("Template '${template.name}' doesn't support language: ${letterRequest.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(letterRequest, template),
            language = letterRequest.language,
            felles = letterRequest.felles
        )
    }

    private fun parseArgument(letterRequest: LetterRequest, template: LetterTemplate<*, *>): Any =
        try {
            objectMapper.treeToValue(letterRequest.letterData, template.parameterType.java)
        } catch (e: JacksonException) {
            throw IllegalArgumentException("Could not parse letterData", e)
        }

}

data class LetterRequest(val template: String, val letterData: ObjectNode, val felles: Felles, val language: Language)