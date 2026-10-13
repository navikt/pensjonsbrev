package no.nav.pensjon.brev.maler.klagebrev

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.SaksbehandlervalgIDSLTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OversendelsesbrevTilKlageinstansPETest {

    @Test
    fun `oversendelsesbrev til klageinstans PE`() {
        LetterTestImpl(
            OversendelsesbrevTilKlageinstansPE.template,
            EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()),
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestPDF(OversendelsesbrevTilKlageinstansPE.kode.name)
    }
}
