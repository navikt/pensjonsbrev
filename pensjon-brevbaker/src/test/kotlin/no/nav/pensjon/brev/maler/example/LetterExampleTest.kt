package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() {
        Letter(
            LetterExample.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL")
    }

    @Test
    fun `test design reference letter`() {
        Letter(
            DesignReferenceLetter.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("DESIGN_REFERENCE_LETTER_BOKMAL")
    }

}