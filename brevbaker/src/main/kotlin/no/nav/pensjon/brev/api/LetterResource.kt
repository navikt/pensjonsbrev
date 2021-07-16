package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.Parameter
import no.nav.pensjon.brev.template.RequiredParameter
import no.nav.pensjon.brev.template.TemplateParameter

data class LetterRequest(val template: String, val arguments: Map<String, JsonNode>)

object LetterResource {
    val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter {
        with(letterRequest) {
            val template = TemplateResource.getTemplate(template) ?: throw IllegalArgumentException("Template not found: $template")
            val parameters = template.let { it.parameters + it.base.parameters }
            val parsedArgs = parseArguments(letterRequest.arguments, parameters)

            return Letter(template, parsedArgs)
        }

    }

    private fun parseArguments(arguments: Map<String, JsonNode>, parameters: Set<TemplateParameter>): Map<Parameter, Any> =
        parameters.associateBy({ it.type }) { objectMapper.treeToValue(arguments[it.type.name], it.type.dataType.java) }
            .filterValues { it != null }

}