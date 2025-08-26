package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class VedtakStansAlderspensjonFlyttingMellomLandTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakStansAlderspensjonFlyttingMellomLand.template,
            Fixtures.create<VedtakStansAlderspensjonFlyttingMellomLandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(VedtakStansAlderspensjonFlyttingMellomLand.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakStansAlderspensjonFlyttingMellomLand.template,
            Fixtures.create<VedtakStansAlderspensjonFlyttingMellomLandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(VedtakStansAlderspensjonFlyttingMellomLand.kode.name)
    }
}