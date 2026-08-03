package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDto
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmOversendelseTilKlageinstans
import org.junit.jupiter.api.Tag

@Tag(TestTags.MANUAL_TEST)
class KlageOrienteringOmOversendelseTilKlageinstansTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            KlageOrienteringOmOversendelseTilKlageinstans.template,
            Fixtures.create<KlageOrienteringOmOversendelseTilKlageinstansDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(KlageOrienteringOmOversendelseTilKlageinstans.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            KlageOrienteringOmOversendelseTilKlageinstans.template,
            Fixtures.create<KlageOrienteringOmOversendelseTilKlageinstansDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestHtml(KlageOrienteringOmOversendelseTilKlageinstans.kode.name)
    }
}