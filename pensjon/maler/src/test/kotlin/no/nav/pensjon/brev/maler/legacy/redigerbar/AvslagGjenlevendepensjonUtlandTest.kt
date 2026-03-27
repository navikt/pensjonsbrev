package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagGjenlevendepensjonUtlandTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagGjenlevendepensjonUtland.template,
            Fixtures.create< AvslagGjenlevendepensjonUtlandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_AVSLAG_GJENLEVENDEPENSJON_UTLAND")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagGjenlevendepensjonUtland.template,
            Fixtures.create< AvslagGjenlevendepensjonUtlandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("GP_AVSLAG_GJENLEVENDEPENSJON_UTLAND")
    }
}