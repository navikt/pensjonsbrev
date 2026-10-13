package no.nav.pensjon.brev.maler.klagebrev

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnhentingAvOpplysningerUTTest {

    private val letter = LetterTestImpl(
        InnhentingAvOpplysningerUT.template,
        EmptyRedigerbarBrevdata(saksbehandlerValg = lagSaksbehandlervalg()),
        Language.Bokmal,
        Fixtures.felles,
    )

    @Test
    fun testPdf() {
        letter.renderTestPDF(InnhentingAvOpplysningerUT.kode.name)
    }

    @Test
    fun testHtml() {
        letter.renderTestHtml(InnhentingAvOpplysningerUT.kode.name)
    }
}
