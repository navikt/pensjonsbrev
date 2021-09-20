package no.nav.pensjon.brev.api

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
            argument = objectMapper.treeToValue(letterRequest.letterData, template.parameterType.java),
            language = letterRequest.language,
            felles = letterRequest.felles
        )
    }

}

data class LetterRequest(val template: String, val letterData: ObjectNode, val felles: Felles, val language: Language)