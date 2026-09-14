package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.avslag.AvslagUfoerepensjonRedigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagUfoerepensjonRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagUfoerepensjonRedigerbar.template,
            Fixtures.create(AvslagUfoerepensjonRedigerbar::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("UP_AVSLAG_UFOERPENSJON")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagUfoerepensjonRedigerbar.template,
            Fixtures.create(AvslagUfoerepensjonRedigerbar::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UP_AVSLAG_UFOERPENSJON")
    }
}