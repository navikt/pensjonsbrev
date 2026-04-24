package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagUfoerepensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagUfoerepensjon.template,
            Fixtures.create<AvslagUfoerepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("GP_AVSLAG_UFOERPENSJON")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagUfoerepensjon.template,
            Fixtures.create<AvslagUfoerepensjonDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml("GP_AVSLAG_UFOERPENSJON")
    }
}