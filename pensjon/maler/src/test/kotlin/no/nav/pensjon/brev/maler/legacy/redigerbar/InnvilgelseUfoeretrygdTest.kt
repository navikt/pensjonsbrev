package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnvilgelseUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseUforetrygd.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_INNVILGELSE_UFOERTRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseUforetrygd.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_INNVILGELSE_UFOERTRYGD")
    }
}