package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktFribelopRedigerbarDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmOktFribelopRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmOktFribelopRedigerbar.template,
            Fixtures.create<VedtakOmOktFribelopRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtakOktFribelopRedigerbar")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmOktFribelopRedigerbar.template,
            Fixtures.create<VedtakOmOktFribelopRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtakOktFribelopRedigerbar")
    }
}
