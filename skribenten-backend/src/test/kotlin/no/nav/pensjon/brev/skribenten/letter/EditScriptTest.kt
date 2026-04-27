package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class EditScriptTest {

    val a = editedLetter(
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mera")))
    )
    val b = editedLetter(
        Paragraph(null, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe merb")))
    )
    private val tokenizer = TextOnlyWordTokenizer()

    @Test
    fun testDiff3() {
        shortestEditScript(tokenizer.tokenize(a), tokenizer.tokenize(b)).forEach { println(it) }
    }


    @Test
    fun bla() {
        val orig = " abc def "
        val split = orig.split(' ')
        println(split)
        val joined = split.joinToString(" ")
        println(joined)
        Assertions.assertEquals(orig, joined)
    }
}