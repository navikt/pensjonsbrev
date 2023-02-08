package no.nav.pensjon.brev.api

import io.ktor.server.plugins.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.template.*

class ParseLetterDataException(msg: String, cause: Exception): Exception(msg, cause)

class LetterResource(val templateResource: TemplateResource = TemplateResource()) {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: VedtaksbrevRequest): Letter<*> {
        val template: LetterTemplate<*, *> = templateResource.getVedtaksbrev(letterRequest.kode)
            ?: throw NotFoundException("Template '${letterRequest.kode}' doesn't exist")

        return create(template, letterRequest.language.toLanguage(), letterRequest.letterData, letterRequest.felles)
    }

    fun create(letterRequest: RedigerbartbrevRequest): Letter<*> {
        val template: LetterTemplate<*, *> = templateResource.getRedigerbartBrev(letterRequest.kode)
            ?: throw NotFoundException("Template '${letterRequest.kode}' doesn't exist")

        return create(template, letterRequest.language.toLanguage(), letterRequest.letterData, letterRequest.felles)
    }

    private fun create(template: LetterTemplate<*, *>, language: Language, letterData: Any, felles: Felles): Letter<*> {
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

    private fun parseArgument(letterData: Any, template: LetterTemplate<*, *>): Any =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not parse letterData: ${e.message}", e)
        }

}
