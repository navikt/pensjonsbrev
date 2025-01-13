package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() {
        Letter(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL")
    }
}