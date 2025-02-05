package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import org.junit.jupiter.api.Test


class EditScriptTest {

    val a = editedLetter(
        Paragraph(1, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mera")))
    )
    val b = editedLetter(
        Paragraph(1, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe merb")))
    )

    @Test
    fun testDiff3() {
        println(Diff3.shortestEditScript(a.editables, b.editables))
    }

}