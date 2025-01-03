package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class ForhaandsvarselVedTilbakekrevingTest {

    @Test
    fun testPdf() {
        Letter(
            ForhaandsvarselVedTilbakekreving.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.English,
            Fixtures.fellesAuto
        ).renderTestPDF(ForhaandsvarselVedTilbakekreving.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            ForhaandsvarselVedTilbakekreving.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml(ForhaandsvarselVedTilbakekreving.kode.name)
    }
}