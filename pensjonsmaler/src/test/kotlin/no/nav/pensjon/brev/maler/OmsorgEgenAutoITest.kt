package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class OmsorgEgenAutoITest {
    @Test
    fun testPdf() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("OMSORG_EGEN_AUTO_BOKMAL")
    }

    @Test
    fun testHtml() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestHtml("OMSORG_EGEN_AUTO_BOKMAL")
    }
}
