package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakEndringGjenlevendepensjonBosattUtlandTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakEndringGjenlevendepensjonBosattUtland.template,
            Fixtures.create<VedtakEndringGjenlevendepensjonBosattUtlandDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestPDF("GP_VEDTAK_ENDRING_BOSATT_UTLAND")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakEndringGjenlevendepensjonBosattUtland.template,
            Fixtures.create<VedtakEndringGjenlevendepensjonBosattUtlandDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestHtml("GP_VEDTAK_ENDRING_BOSATT_UTLAND")
    }
}

