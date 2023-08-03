package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.INTEGRATION_TEST)
class ForhaandsvarselEtteroppgjoerUfoeretrygdAutoTest {
    @Test
    fun testPdf() {
        Letter(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_EO_FORHAANDSVARSEL_AUTO")
    }

    @Test
    fun testHtml() {
        Letter(
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_EO_FORHAANDSVARSEL_AUTO")
    }
}