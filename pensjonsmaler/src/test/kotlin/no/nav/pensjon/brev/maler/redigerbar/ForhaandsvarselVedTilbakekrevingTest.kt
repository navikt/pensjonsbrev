package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class ForhaandsvarselVedTilbakekrevingTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            ForhaandsvarselVedTilbakekreving.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(ForhaandsvarselVedTilbakekreving.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            ForhaandsvarselVedTilbakekreving.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(ForhaandsvarselVedTilbakekreving.kode.name)
    }
}