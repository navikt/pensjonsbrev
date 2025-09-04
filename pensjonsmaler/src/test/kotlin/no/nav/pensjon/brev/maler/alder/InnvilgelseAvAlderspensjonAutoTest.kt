package no.nav.pensjon.brev.maler.alder

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class InnvilgelseAvAlderspensjonAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseAvAlderspensjonAuto.template,
            Fixtures.create<InnvilgelseAvAlderspensjonAutoDto>(),
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