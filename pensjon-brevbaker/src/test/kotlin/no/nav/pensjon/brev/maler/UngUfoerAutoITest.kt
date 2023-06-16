package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class UngUfoerAutoITest {

    @Test
    fun pdftest() {
        Letter(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UNG_UFOER_AUTO_BOKMAL")
    }

    @Test
    fun testHtml() {
        Letter(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UNG_UFOER_AUTO_BOKMAL")
    }

}