package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OrienteringOmForlengetSaksbehandlingTest {

    @Test
    fun testPdf() {
        Letter(
            OrienteringOmForlengetSaksbehandlingstid.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.English,
            Fixtures.felles
        ).renderTestPDF(OrienteringOmForlengetSaksbehandlingstid.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            OrienteringOmForlengetSaksbehandlingstid.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(OrienteringOmForlengetSaksbehandlingstid.kode.name)
    }
}