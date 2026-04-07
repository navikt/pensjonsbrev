package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland.template,
            Fixtures.create<InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland.template,
            Fixtures.create<InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND")
    }
}
