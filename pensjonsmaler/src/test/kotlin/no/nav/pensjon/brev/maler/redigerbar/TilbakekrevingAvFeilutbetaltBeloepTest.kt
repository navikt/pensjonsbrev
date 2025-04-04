package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class TilbakekrevingAvFeilutbetaltBeloepTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            TilbakekrevingAvFeilutbetaltBeloep.template,
            Fixtures.create<TilbakekrevingAvFeilutbetaltBeloepDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestPDF(TilbakekrevingAvFeilutbetaltBeloep.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            TilbakekrevingAvFeilutbetaltBeloep.template,
            Fixtures.create<TilbakekrevingAvFeilutbetaltBeloepDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(TilbakekrevingAvFeilutbetaltBeloep.kode.name)
    }
}