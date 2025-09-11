package no.nav.pensjon.brev.maler.alder

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class EndringAvUttaksgradAutoTest {
    @Test
        fun testPdf() {
            LetterTestImpl(
                EndringAvUttaksgradAuto.template,
                Fixtures.create<EndringAvUttaksgradAutoDto>(),
                Language.Bokmal,
                Fixtures.felles
            ).renderTestPDF(InnvilgelseAvAlderspensjonAuto.kode.name)
        }

        @Test
        fun testHtml() {
            LetterTestImpl(
                InnvilgelseAvAlderspensjonAuto.template,
                Fixtures.create<InnvilgelseAvAlderspensjonAutoDto>(),
                Language.Nynorsk,
                Fixtures.felles
            ).renderTestHtml(InnvilgelseAvAlderspensjonAuto.kode.name)
        }
    }