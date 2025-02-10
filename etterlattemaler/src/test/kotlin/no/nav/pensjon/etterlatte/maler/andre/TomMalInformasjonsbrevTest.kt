package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.TestTags
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.renderTestHtml
import no.nav.pensjon.etterlatte.renderTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class TomMalInformasjonsbrevTest {

    @Test
    fun pdftest() {
        val letter = Letter(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        )
        letter.renderTestPDF(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name)
    }

    @Test
    fun testHtml() {
        Letter(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name)
    }

}