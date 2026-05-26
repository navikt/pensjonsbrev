package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDto
import no.nav.pensjon.brev.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026Redigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmEtterbetalingOpphor2026Test {

    @Test
    fun `VedtakOmEtterbetalingOpphor2026Auto - bokmål - HTML`() {
        LetterTestImpl(
            VedtakOmEtterbetalingOpphor2026Auto.template,
            Fixtures.create<VedtakOmEtterbetalingOpphor2026AutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("VEDTAK_ETTERBETALING_OPPHOR_2026_AUTO_BM")
    }

    @Test
    fun `VedtakOmEtterbetalingOpphor2026Auto - nynorsk - HTML`() {
        LetterTestImpl(
            VedtakOmEtterbetalingOpphor2026Auto.template,
            Fixtures.create<VedtakOmEtterbetalingOpphor2026AutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("VEDTAK_ETTERBETALING_OPPHOR_2026_AUTO_NN")
    }

    @Test
    fun `VedtakOmEtterbetalingOpphor2026Redigerbar - bokmål - HTML`() {
        LetterTestImpl(
            VedtakOmEtterbetalingOpphor2026Redigerbar.template,
            Fixtures.create<VedtakOmEtterbetalingOpphor2026RedigerbarDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("VEDTAK_ETTERBETALING_OPPHOR_2026_REDIGERBAR_BM")
    }

    @Test
    fun `VedtakOmEtterbetalingOpphor2026Redigerbar - nynorsk - HTML`() {
        LetterTestImpl(
            VedtakOmEtterbetalingOpphor2026Redigerbar.template,
            Fixtures.create<VedtakOmEtterbetalingOpphor2026RedigerbarDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestPDF("VEDTAK_ETTERBETALING_OPPHOR_2026_REDIGERBAR_NN")
    }
}
