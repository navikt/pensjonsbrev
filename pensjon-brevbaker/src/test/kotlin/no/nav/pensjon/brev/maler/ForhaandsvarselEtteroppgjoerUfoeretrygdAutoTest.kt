package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class ForhaandsvarselEtteroppgjoerUfoeretrygdAutoTest {
    @Test
    fun testPdf() {
        renderTestPDF(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>(),
            Language.English,
            Fixtures.fellesAuto,
            "UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            "UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO"
        )
    }
}