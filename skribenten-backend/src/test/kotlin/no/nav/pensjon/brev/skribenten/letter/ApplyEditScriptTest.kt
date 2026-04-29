package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import org.junit.jupiter.api.Test

class ApplyEditScriptTest {

    val letter1 = editedLetter {
        paragraph(id = 2) {
            literal(id = 1, text = "noe tekst ")
            variable(id = 2, text = " og noe mer veldig dyrt")
        }
    }
    val letter2 = editedLetter {
        paragraph { literal(text = "jadda") }
        paragraph(id = 2) {
            literal(id = 1, text = "noe tekst ")
            variable(id = 2, text = " og noe mer kjempe billig")
        }
    }

    @Test
    fun `can generate delete DiffSegments`() {
        val diff = EditLetterWordDiff().diff(old = letter1.toMarkup().toEdit(), new = letter2)

        jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(LetterMarkupJacksonModule)
        }.writerWithDefaultPrettyPrinter()
            .writeValueAsString(letter1.toMarkup().blocks)
            .also { println(it) }

        println()
        println("Diff highlight")
        println("==============")
        diff.second.forEach { println(it) }
    }

    @Test
    fun `can generate insert DiffSegments`() {
        val diff = EditLetterWordDiff().diff(old = letter1.toMarkup().toEdit(), new = letter2)

        jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(LetterMarkupJacksonModule)
        }.writerWithDefaultPrettyPrinter()
            .writeValueAsString(letter2.blocks)
            .also { println(it) }

        println()
        println("Diff highlight")
        println("==============")
        diff.first.forEach { println(it) }
    }
}