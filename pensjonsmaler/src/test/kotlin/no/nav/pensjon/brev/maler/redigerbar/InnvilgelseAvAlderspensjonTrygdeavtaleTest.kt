package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class InnvilgelseAvAlderspensjonTrygdeavtaleTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseAvAlderspensjonTrygdeavtale.template,
            Fixtures.create<InnvilgelseAvAlderspensjonTrygdeavtaleDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(InnvilgelseAvAlderspensjonTrygdeavtale.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseAvAlderspensjonTrygdeavtale.template,
            Fixtures.create<InnvilgelseAvAlderspensjonTrygdeavtaleDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(InnvilgelseAvAlderspensjonTrygdeavtale.kode.name)
    }
}