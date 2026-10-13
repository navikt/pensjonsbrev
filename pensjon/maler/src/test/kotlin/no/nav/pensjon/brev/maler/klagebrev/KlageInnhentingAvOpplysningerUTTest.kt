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
class KlageInnhentingAvOpplysningerUTTest {

    private val letter = LetterTestImpl(
        KlageInnhentingAvOpplysningerUT.template,
        EmptyRedigerbarBrevdata(saksbehandlerValg = lagSaksbehandlervalg()),
        Language.Bokmal,
        Fixtures.felles,
    )

    @Test
    fun testPdf() {
        letter.renderTestPDF(KlageInnhentingAvOpplysningerUT.kode.name)
    }

    @Test
    fun testHtml() {
        letter.renderTestHtml(KlageInnhentingAvOpplysningerUT.kode.name)
    }
}
