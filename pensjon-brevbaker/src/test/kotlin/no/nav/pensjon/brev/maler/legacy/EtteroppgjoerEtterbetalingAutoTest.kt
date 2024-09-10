package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EtteroppgjoerEtterbetalingAutoTest {

    @Test
    fun testPdf() {
        Letter(
            EtteroppgjoerEtterbetalingAuto.template,
            Fixtures.create<EtteroppgjoerEtterbetalingAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRET_PGA_INNTEKT")
    }

    @Test
    fun testHtml() {
        Letter(
            EtteroppgjoerEtterbetalingAuto.template,
            Fixtures.create<EtteroppgjoerEtterbetalingAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRET_PGA_INNTEKT")
    }
}