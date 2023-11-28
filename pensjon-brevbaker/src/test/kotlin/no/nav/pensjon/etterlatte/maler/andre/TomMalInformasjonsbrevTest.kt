package no.nav.pensjon.etterlatte.maler.andre

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestHTML
import no.nav.pensjon.brev.writeTestPDF
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class TomMalInformasjonsbrevTest {

    @Test
    fun pdftest() {
        Letter(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name, it) }
    }

    @Test
    fun testHtml() {
        Letter(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name, it) }
    }

}