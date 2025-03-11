package no.nav.pensjon.etterlatte.maler.andre

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
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
        val letter = LetterTestImpl(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        )
        letter.renderTestPDF(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name)
    }

}