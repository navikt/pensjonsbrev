package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class ForhaandsvarselEtteroppgjoerUfoeretrygdAutoTest {
    @Test
    fun testPdf() {
        Letter(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>(),
            Language.English,
            Fixtures.fellesAuto,
        ).renderTestPDF("UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO")
    }

    @Test
    fun testHtml() {
        Letter(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
        ).renderTestHtml("UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO")
    }
}
