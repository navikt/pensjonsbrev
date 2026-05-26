package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrev
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.brev.brevbaker.FeatureToggleDummy
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import no.nav.brev.brevbaker.TestTags

class AfpVarianterPdfTest {

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `pdf med AFP privat`() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brevDto = Fixtures.createBrevDtoMedAfpPrivat()
        LetterTestImpl(ApSimuleringBrev.template, brevDto, Language.Bokmal, FellesFactory.felles)
            .renderTestPDF("AP_SIMULERING_AFP_PRIVAT", pdfByggerService = PdfByggerTestService())
    }

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `pdf med AFP offentlig livsvarig`() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brevDto = Fixtures.createBrevDtoMedAfpOffentligLivsvarig()
        LetterTestImpl(ApSimuleringBrev.template, brevDto, Language.Bokmal, FellesFactory.felles)
            .renderTestPDF("AP_SIMULERING_AFP_OFFENTLIG_LIVSVARIG", pdfByggerService = PdfByggerTestService())
    }

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `pdf med AFP offentlig tidsbegrenset`() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brevDto = Fixtures.createBrevDtoMedAfpOffentligTidsbegrenset()
        LetterTestImpl(ApSimuleringBrev.template, brevDto, Language.Bokmal, FellesFactory.felles)
            .renderTestPDF("AP_SIMULERING_AFP_OFFENTLIG_TIDSBEGRENSET", pdfByggerService = PdfByggerTestService())
    }
}
