package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class OmsorgEgenAutoITest {

    @Test
    fun testPdf() {
        renderTestPDF(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "OMSORG_EGEN_AUTO_BOKMAL"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "OMSORG_EGEN_AUTO_BOKMAL"
        )
    }
}