package no.nav.pensjon.brev.api

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.node.ObjectNode
import io.ktor.features.*
import no.nav.pensjon.brev.api.dto.toLanguage
import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

object LetterResource {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: LetterRequest): Letter<*> {
        val template: LetterTemplate<*, *> = TemplateResource.getTemplate(letterRequest.template)
            ?: throw NotFoundException("Template '${letterRequest.template}' doesn't exist")

        val language = letterRequest.language.toLanguage()
        if (!template.language.supports(language)) {
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
            objectMapper.treeToValue(letterRequest.letterData, template.letterDataType.java)
        } catch (e: JacksonException) {
            throw IllegalArgumentException("Could not parse letterData", e)
        }

}
