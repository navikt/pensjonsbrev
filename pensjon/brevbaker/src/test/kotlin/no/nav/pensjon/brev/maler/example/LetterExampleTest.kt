package no.nav.pensjon.brev.maler.example

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class LetterExampleTest {

    @Test
    fun test() {
        LetterImpl(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            FellesFactory.fellesAuto
        ).renderTestPDF("EKSEMPELBREV_BOKMAL")
    }
}