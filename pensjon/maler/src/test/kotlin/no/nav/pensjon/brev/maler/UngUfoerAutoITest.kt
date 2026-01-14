package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class UngUfoerAutoITest {

    @Test
    fun pdftest() {
        LetterTestImpl(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UNG_UFOER_AUTO_BOKMAL")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UNG_UFOER_AUTO_BOKMAL")
    }

}