package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.VedtakFeilutbetaling
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel.VarselFeilutbetaling
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselFeilutbetalingTest {
    @Test
    fun testHtmlBokmal() {
        LetterTestImpl(
            VarselFeilutbetaling.template,
            Fixtures.create<VarselFeilutbetalingUforeDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(VarselFeilutbetaling.kode.name)
    }
    @Test
    fun testHtmlNynorsk() {
        LetterTestImpl(
            VarselFeilutbetaling.template,
            Fixtures.create<VarselFeilutbetalingUforeDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml(VarselFeilutbetaling.kode.name)
    }
}