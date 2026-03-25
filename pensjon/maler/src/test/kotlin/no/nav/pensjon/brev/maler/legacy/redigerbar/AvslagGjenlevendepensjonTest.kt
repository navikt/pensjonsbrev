package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagGjenlevendepensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagGjenlevendepensjon.template,
            Fixtures.create<AvslagGjenlevendepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_AVSLAG_GJENLEVENDEPENSJON")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagGjenlevendepensjon.template,
            Fixtures.create<AvslagGjenlevendepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("GP_AVSLAG_GJENLEVENDEPENSJON")
    }
}