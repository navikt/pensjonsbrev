package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.fixtures.createLetterExampleForenklaDto
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.testBrevbakerApp
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() = testBrevbakerApp {
        Letter(
            LetterExample.template,
            LetterExample.konverter(createLetterExampleForenklaDto()),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL")
    }
}