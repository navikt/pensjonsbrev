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
        renderTestPDF(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        "UNG_UFOER_AUTO_BOKMAL"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            "UNG_UFOER_AUTO_BOKMAL"
        )
    }

}