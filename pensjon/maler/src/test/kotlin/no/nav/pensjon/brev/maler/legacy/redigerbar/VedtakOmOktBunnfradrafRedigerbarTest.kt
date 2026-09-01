package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktBunnfradragRedigerbarDto
import no.nav.pensjon.brev.maler.ufoereBrev.regelendr26.red.VedtakOmOktBunnfradragRedigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmOktBunnfradragRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmOktBunnfradragRedigerbar.template,
            Fixtures.create<VedtakOmOktBunnfradragRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("vedtakOktBunnfradragRedigerbar")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmOktBunnfradragRedigerbar.template,
            Fixtures.create<VedtakOmOktBunnfradragRedigerbarDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("vedtakOktBunnfradragRedigerbar")
    }
}
