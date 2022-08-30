package no.nav.pensjon.brev.maler.example

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class LetterExampleTest {

    @Test
    fun test() {
        Letter(
            LetterExample.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("EKSEMPELBREV_BOKMAL", it) }
    }

    @Test
    fun `test design reference letter`() {
        Letter(
            DesignReferenceLetter.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("DESIGN_REFERENCE_LETTER_BOKMAL", it) }
    }

}