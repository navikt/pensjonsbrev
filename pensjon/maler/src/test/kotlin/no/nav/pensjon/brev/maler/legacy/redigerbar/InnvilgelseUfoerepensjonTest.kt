package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoerepensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnvilgelseUfoerepensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseUfoerepensjon.template,
            Fixtures.create< InnvilgelseUfoerepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("UT_INNVILGELSE_UFOEREPENSJON")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseUfoerepensjon.template,
            Fixtures.create< InnvilgelseUfoerepensjonDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UT_INNVILGELSE_UFOEREPENSJON")
    }
}