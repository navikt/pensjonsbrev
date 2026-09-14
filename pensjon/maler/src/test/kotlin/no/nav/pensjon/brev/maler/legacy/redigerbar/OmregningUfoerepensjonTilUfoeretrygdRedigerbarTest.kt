package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.diverse.OmregningUfoerepensjonTilUfoeretrygdRedigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OmregningUfoerepensjonTilUfoeretrygdRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            OmregningUfoerepensjonTilUfoeretrygdRedigerbar.template,
            Fixtures.create(OmregningUfoerepensjonTilUfoeretrygdRedigerbar::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            OmregningUfoerepensjonTilUfoeretrygdRedigerbar.template,
            Fixtures.create(OmregningUfoerepensjonTilUfoeretrygdRedigerbar::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD")
    }
}