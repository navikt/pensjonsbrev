package no.nav.pensjon.etterlatte.maler.andre

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
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
        val letter = Letter(
            TomMalInformasjonsbrev.template,
            Fixtures.create<ManueltBrevMedTittelDTO>(),
            Language.Bokmal,
            Fixtures.felles
        )
        Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter.language, letter.felles, letter.template.letterMetadata.brevtype) }
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
        ).let { HTMLDocumentRenderer.render(it) }
            .also { writeTestHTML(EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV.name, it) }
    }

}