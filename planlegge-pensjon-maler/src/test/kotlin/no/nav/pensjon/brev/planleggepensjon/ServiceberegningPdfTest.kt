package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FeatureToggleDummy
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.ServiceberegningBrev
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class ServiceberegningPdfTest {

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `pdf med serviceberegning AFP`() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brevDto = Fixtures.createServiceberegningBrevDto()
        LetterTestImpl(ServiceberegningBrev.template, brevDto, Language.Bokmal, Fixtures.felles)
            .renderTestPDF("SERVICEBEREGNING_AFP", pdfByggerService = PdfByggerTestService())
    }
}
