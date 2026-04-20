package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmLavereMinstesatsRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmLavereMinstesatsRedigerbar.template,
            Fixtures.create<VedtakOmLavereMinstesatsRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtakLavereMinstesatsRedigerbar")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmLavereMinstesatsRedigerbar.template,
            Fixtures.create<VedtakOmLavereMinstesatsRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtakLavereMinstesatsRedigerbar")
    }
}
