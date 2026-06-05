package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmEndringBarnetilleggEPSAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmEndringBarnetilleggEPSAuto.template,
            Fixtures.create<VedtakOmEndringBarnetilleggEPSAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtak")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmEndringBarnetilleggEPSAuto.template,
            Fixtures.create<VedtakOmEndringBarnetilleggEPSAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtak")
    }
}
