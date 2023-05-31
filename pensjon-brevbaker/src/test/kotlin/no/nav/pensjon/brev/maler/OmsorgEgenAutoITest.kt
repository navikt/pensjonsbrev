package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.VISUAL_TEST)
class OmsorgEgenAutoITest {

    @Test
    fun testPdf() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OMSORG_EGEN_AUTO_BOKMAL")
    }

    @Test
    fun testHtml() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("OMSORG_EGEN_AUTO_BOKMAL", it) }
    }
}