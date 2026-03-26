package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpphoerGjenlevendepensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            OpphoerGjenlevendepensjon.template,
            Fixtures.create<OpphoerGjenlevendepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_OPPHOER_GJENLEVENDEPENSJON")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            OpphoerGjenlevendepensjon.template,
            Fixtures.create<OpphoerGjenlevendepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("GP_OPPHOER_GJENLEVENDEPENSJON")
    }
}