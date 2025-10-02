package no.nav.pensjon.brev.maler.alder

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class EndringAvAlderspensjonFordiDuFyller75AarAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndringAvAlderspensjonFordiDuFyller75AarAuto.template,
            Fixtures.create<EndringAvAlderspensjonFordiDuFyller75AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto
        ).renderTestPDF(EndringAvAlderspensjonFordiDuFyller75AarAuto.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndringAvAlderspensjonFordiDuFyller75AarAuto.template,
            Fixtures.create<EndringAvAlderspensjonFordiDuFyller75AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml(EndringAvAlderspensjonFordiDuFyller75AarAuto.kode.name)
    }
}