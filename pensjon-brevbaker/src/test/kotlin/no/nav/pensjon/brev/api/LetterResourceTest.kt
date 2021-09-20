package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310StringParsableDeserializer
import io.ktor.features.*
import no.nav.pensjon.brev.api.dto.*
import no.nav.pensjon.brev.felles
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.maler.EksempelBrevDto
import no.nav.pensjon.brev.something.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

class LetterResourceTest {

    val returAdresse = ReturAdresse(
        adresseLinje1 = "TESTadresseLinje1",
        postNr = "TESTpostNr",
        postSted = "TESTpostSted",
    )
    val template = EksempelBrev.template
    val eksempelBrevDto = objectMapper.convertValue(
        EksempelBrevDto(
            pensjonInnvilget = true,
            datoInnvilget = LocalDate.of(2020, 1, 1)
        ), ObjectNode::class.java
    )

    @Test
    fun `create finds correct template`() {
        val letter = LetterResource.create(LetterRequest(template.name, eksempelBrevDto, felles, Language.Bokmal))

        assertEquals(template, letter.template)
    }

    @Test
    fun `create fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource.create(LetterRequest("non existing", eksempelBrevDto, felles, Language.Bokmal))
        }
    }

    @Test
    fun `create requires arguments`() {
        assertThrows<IllegalArgumentException> {
            val emptyObjectNode = objectMapper.convertValue(Object(), ObjectNode::class.java)
            LetterResource.create(LetterRequest(template.name, emptyObjectNode, felles, Language.Bokmal))
        }
    }

    @Test
    fun `create parses arguments`() {
        val letter = LetterResource.create(LetterRequest(template.name, eksempelBrevDto, felles, Language.Bokmal))
        assertEquals(returAdresse, letter.felles.avsenderEnhet.returAdresse)
    }

//    @Test
//    fun `create parses arguments but does not add null values`() {
//        val argsWithoutPensjonInnvilget = templateArgs.filterKeys { it != PensjonInnvilget.name }
//        val letter = LetterResource.create(LetterRequest(template.name, argsWithoutPensjonInnvilget, Language.Bokmal))
//
//        assertFalse(
//            letter.argument.containsKey(PensjonInnvilget)
//        )
//    }
}