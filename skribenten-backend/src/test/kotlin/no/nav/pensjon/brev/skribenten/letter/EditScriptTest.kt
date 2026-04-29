package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class EditScriptTest {

    val a = editedLetter {
        paragraph(id = 2) {
            literal(id = 1, text = "noe tekst ")
            variable(id = 2, text = " og noe mera")
        }
    }
    val b = editedLetter {
        paragraph { literal(text = "jadda") }
        paragraph(id = 2) {
            literal(id = 1, text = "noe tekst ")
            variable(id = 2, text = " og noe merb")
        }
    }
    private val tokenizer = EditLetterWordDiff()

    @Test
    fun testDiff3() {
        shortestEditScript(tokenizer.tokenize(a), tokenizer.tokenize(b)).all.forEach { println(it) }
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