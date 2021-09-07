package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.features.*
import no.nav.pensjon.brev.template.*

data class LetterRequest(val template: String, val arguments: Map<String, JsonNode>, val language: Language)

object LetterResource {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter {
        val template = TemplateResource.getTemplate(letterRequest.template)
            ?: throw NotFoundException("Template '${letterRequest.template}' doesn't exist")

        if (!template.language.supports(letterRequest.language)) {
            throw IllegalArgumentException("Template '${template.name}' doesn't support language: ${letterRequest.language}")
        }

        return Letter(
            template = template,
            arguments = parseArguments(letterRequest.arguments, template.parameters + template.base.parameters),
            language = letterRequest.language
        )
    }

    private fun parseArguments(
        arguments: Map<String, JsonNode>,
        parameters: Set<TemplateParameter>
    ): Map<Parameter, Any> =
        parameters.associateBy({ it.parameter }) {
            objectMapper.treeToValue(
                arguments[it.parameter.name],
                it.parameter.dataType.java
            )
        }.filterValues { it != null }

}