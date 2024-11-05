package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagUfoeretrygdTest {

    @Test
    fun testPdf() {
        Letter(
            AvslagUfoeretrygd.template,
            Fixtures.create<AvslagUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_AVSLAG_UFOERTRYGD")
    }

    @Test
    fun testHtml() {
        Letter(
            AvslagUfoeretrygd.template,
            Fixtures.create<AvslagUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_AVSLAG_UFOERTRYGD")
    }
}