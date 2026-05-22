package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.FeatureToggleDummy
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrev
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class GrunnlagVedleggPdfTest {

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `pdf med grunnlag vedlegg`() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brevDto = Fixtures.createBrevDtoMedAfpPrivat()
        LetterTestImpl(ApSimuleringBrev.template, brevDto, Language.Bokmal, FellesFactory.felles)
            .renderTestPDF("AP_SIMULERING_GRUNNLAG", pdfByggerService = PdfByggerTestService())
    }
}
