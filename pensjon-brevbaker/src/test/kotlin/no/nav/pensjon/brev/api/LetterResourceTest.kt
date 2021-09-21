package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.convertValue
import io.ktor.features.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.maler.EksempelBrevDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

class LetterResourceTest {

    val template = EksempelBrev.template
    val eksempelBrevDto = objectMapper.convertValue<ObjectNode>(
        EksempelBrevDto(
            pensjonInnvilget = true,
            datoInnvilget = LocalDate.of(2020, 1, 1)
        )
    )

    @Test
    fun `create finds correct template`() {
        val letter =
            LetterResource.create(LetterRequest(template.name, eksempelBrevDto, Fixtures.felles, Language.Bokmal))

        assertEquals(template, letter.template)
    }

    @Test
    fun `create fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource.create(LetterRequest("non existing", eksempelBrevDto, Fixtures.felles, Language.Bokmal))
        }
    }

    @Test
    fun `create requires arguments`() {
        val emptyObjectNode = objectMapper.convertValue<ObjectNode>(emptyMap<String, String>())
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest(template.name, emptyObjectNode, Fixtures.felles, Language.Bokmal))
        }
    }

    @Test
    fun `create parses arguments`() {
        val letter =
            LetterResource.create(LetterRequest(template.name, eksempelBrevDto, Fixtures.felles, Language.Bokmal))
        assertEquals(Fixtures.felles.avsenderEnhet.returAdresse, letter.felles.avsenderEnhet.returAdresse)
    }

    @Test
    fun `create fails when letterData is invalid`() {
        val invalidData = objectMapper.convertValue<ObjectNode>(mapOf("pensjonInnvilget" to true))
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest(template.name, invalidData, Fixtures.felles, Language.Bokmal))
        }
    }

    @Test
    fun `create fails for unsupported language`() {
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest(template.name, eksempelBrevDto, Fixtures.felles, Language.Nynorsk))
        }
    }

}