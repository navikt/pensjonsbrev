package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.VedtakOmLavereMinstesatsAuto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmLavereMinstesatsAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmLavereMinstesatsAuto.template,
            Fixtures.create(VedtakOmLavereMinstesatsAuto::class),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtak")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmLavereMinstesatsAuto.template,
            Fixtures.create(VedtakOmLavereMinstesatsAuto::class),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtak")
    }
}