package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmSaksbehandlingstid
import org.junit.jupiter.api.Tag

@Tag(TestTags.MANUAL_TEST)
class KlageOrienteringOmSaksbehandlingstid {

    @Test
    fun testPdf() {
        LetterTestImpl(
            KlageOrienteringOmSaksbehandlingstid.template,
            Fixtures.create<KlageOrienteringOmSaksbehandlingstidDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(KlageOrienteringOmSaksbehandlingstid.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            KlageOrienteringOmSaksbehandlingstid.template,
            Fixtures.create<KlageOrienteringOmSaksbehandlingstidDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestHtml(KlageOrienteringOmSaksbehandlingstid.kode.name)
    }
}