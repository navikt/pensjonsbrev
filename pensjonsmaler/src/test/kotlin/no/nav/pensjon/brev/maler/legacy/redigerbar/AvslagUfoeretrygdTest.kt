package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagUfoeretrygd.template,
            Fixtures.create<AvslagUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_AVSLAG_UFOERTRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagUfoeretrygd.template,
            Fixtures.create<AvslagUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_AVSLAG_UFOERTRYGD")
    }
}