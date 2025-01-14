package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() {
        renderTestPDF(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "EKSEMPELBREV_BOKMAL"
        )
    }

    @Test
    fun `test design reference letter`() {
        renderTestPDF(
            DesignReferenceLetter.template,
            createLetterExampleDto(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "DESIGN_REFERENCE_LETTER_BOKMAL"
        )
    }

}