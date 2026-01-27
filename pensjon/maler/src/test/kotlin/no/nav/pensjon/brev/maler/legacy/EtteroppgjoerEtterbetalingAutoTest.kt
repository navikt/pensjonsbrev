package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EtteroppgjoerEtterbetalingAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EtteroppgjoerEtterbetalingAutoLegacy.template,
            Fixtures.create<EtteroppgjoerEtterbetalingAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRET_PGA_INNTEKT")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EtteroppgjoerEtterbetalingAutoLegacy.template,
            Fixtures.create<EtteroppgjoerEtterbetalingAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRET_PGA_INNTEKT")
    }
}