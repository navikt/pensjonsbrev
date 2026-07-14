package no.nav.pensjon.brev.ufore.maler.simulering

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path

class SimuleringUforetrygdTest {

    @Tag(TestTags.INTEGRATION_TEST)
    @Test
    fun `simulering med yrkesskade rendres`() {
        LetterTestImpl(
            template = SimuleringUforetrygd.template,
            argument = Fixtures.lagSimuleringUforetrygdMedYrkesskade(),
            language = Language.Bokmal,
            felles = FellesFactory.fellesAuto,
        ).renderTestPDF(
            "${SimuleringUforetrygd.kode.name}_Yrkesskade_Bokmal",
            path = Path.of("build", "test_pdf_typst"),
            pdfByggerService = PdfByggerTestService(),
        )
    }
}
