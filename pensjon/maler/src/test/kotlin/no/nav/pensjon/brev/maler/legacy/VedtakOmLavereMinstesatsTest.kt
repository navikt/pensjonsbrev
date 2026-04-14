package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmLavereMinstesatsTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmLavereMinstesats.template,
            Fixtures.create<VedtakOmLavereMinstesatsDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtak")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmLavereMinstesats.template,
            Fixtures.create<VedtakOmLavereMinstesatsDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtak")
    }
}