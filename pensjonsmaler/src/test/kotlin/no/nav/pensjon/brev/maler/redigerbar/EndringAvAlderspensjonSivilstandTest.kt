package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class EndringAvAlderspensjonSivilstandTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndringAvAlderspensjonSivilstand.template,
            Fixtures.create<EndringAvAlderspensjonSivilstandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(EndringAvAlderspensjonSivilstand.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndringAvAlderspensjonSivilstand.template,
            Fixtures.create<EndringAvAlderspensjonSivilstandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(EndringAvAlderspensjonSivilstand.kode.name)
    }
}