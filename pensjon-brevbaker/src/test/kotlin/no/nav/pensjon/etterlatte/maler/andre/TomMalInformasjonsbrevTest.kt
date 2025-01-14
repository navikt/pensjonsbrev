package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class TomMalInformasjonsbrevTest {

    @Test
    fun pdftest() {
        renderTestPDF(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles,
            EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles,
            EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name
        )
    }

}