package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndringUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndringUforetrygd.template,
            Fixtures.create<EndringUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRING_UFOERETRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndringUforetrygd.template,
            Fixtures.create<EndringUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRING_UFOERETRYGD")
    }
}