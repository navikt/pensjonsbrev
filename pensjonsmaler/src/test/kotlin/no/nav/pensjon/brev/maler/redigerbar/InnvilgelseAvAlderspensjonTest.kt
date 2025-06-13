package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class InnvilgelseAvAlderspensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseAvAlderspensjon.template,
            Fixtures.create<InnvilgelseAvAlderspensjonDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestPDF(InnvilgelseAvAlderspensjon.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseAvAlderspensjon.template,
            Fixtures.create<InnvilgelseAvAlderspensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(InnvilgelseAvAlderspensjon.kode.name)
    }
}