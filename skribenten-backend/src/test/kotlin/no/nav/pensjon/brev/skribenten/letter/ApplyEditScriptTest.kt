package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import org.junit.jupiter.api.Test

class ApplyEditScriptTest {

    val letter1 = editedLetter(
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mer veldig dyrt")))
    )
    val letter2 = editedLetter(
        Paragraph(null, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mer kjempe billig")))
    )

    @Test
    fun `can generate delete DiffSegments`() {
        val diff = diffBrev(letter2, letter1.toMarkup())

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
        val diff = diffBrev(letter2, letter1.toMarkup())

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