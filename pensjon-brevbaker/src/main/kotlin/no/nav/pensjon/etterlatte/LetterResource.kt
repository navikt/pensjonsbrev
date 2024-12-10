package no.nav.pensjon.etterlatte

import io.ktor.server.plugins.*
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.Felles

class LetterResource(private val templateResource: TemplateResource = TemplateResource()) {
    private val objectMapper = jacksonObjectMapper()

    fun create(letterRequest: EtterlatteBrevRequest): Letter<*> {
        val template: LetterTemplate<*, *> = templateResource.getAutoBrev(letterRequest.kode)
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
