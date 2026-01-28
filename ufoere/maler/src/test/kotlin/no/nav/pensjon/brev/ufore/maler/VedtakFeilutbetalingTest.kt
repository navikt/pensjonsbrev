package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakFeilutbetaling
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakFeilutbetalingTest {
    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakFeilutbetaling.template,
            Fixtures.create<VedtakFeilutbetalingUforeDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml(VedtakFeilutbetaling.kode.name)
    }
}