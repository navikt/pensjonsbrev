package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmregningUfoerepensjonTilUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OmregningUfoerepensjonTilUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            OmregningUfoerepensjonTilUfoeretrygd.template,
            Fixtures.create<OmregningUfoerepensjonTilUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            OmregningUfoerepensjonTilUfoeretrygd.template,
            Fixtures.create<OmregningUfoerepensjonTilUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD")
    }
}