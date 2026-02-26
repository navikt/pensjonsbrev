package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakFeilutbetaling
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDto
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakIngenTilbakekreving
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakIngenTilbakekrevingTest {
    @Test
    fun testHtmlBokmal() {
        LetterTestImpl(
            VedtakIngenTilbakekreving.template,
            Fixtures.create<VedtakFeilutbetalingUforeIngenTilbakekrevingDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(VedtakIngenTilbakekreving.kode.name)
    }
    @Test
    fun testHtmlNynorsk() {
        LetterTestImpl(
            VedtakIngenTilbakekreving.template,
            Fixtures.create<VedtakFeilutbetalingUforeIngenTilbakekrevingDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml(VedtakIngenTilbakekreving.kode.name)
    }
}