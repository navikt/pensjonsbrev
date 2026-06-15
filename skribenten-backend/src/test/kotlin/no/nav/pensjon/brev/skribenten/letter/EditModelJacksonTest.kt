package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EditModelJacksonTest {

    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        registerModule(LetterMarkupJacksonModule)

        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Test
    fun `kan deserialisere gjeldende EditModel`() {
        val expected = editedLetter {
            title1 { literal(null, "Mitt avsnitt") }
            paragraph {
                literal(null, text = "hei")
                variable(null, "joda")
                itemList {
                    item { literal(null, "kanskje") }
                }
            }
        }
        val json = objectMapper.writeValueAsString(expected)
        val actual = objectMapper.readValue(json, Edit.Letter::class.java)

        assertThat(actual).isEqualTo(expected)
    }
}