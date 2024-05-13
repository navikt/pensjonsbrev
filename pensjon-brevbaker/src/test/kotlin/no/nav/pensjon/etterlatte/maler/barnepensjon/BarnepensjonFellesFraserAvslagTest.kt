package no.nav.pensjon.etterlatte.maler.barnepensjon

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
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslag
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class BarnepensjonAvslagTest {

    @Test
    fun pdftest() {
        val letter = Letter(
            BarnepensjonAvslag.template,
            Fixtures.create<BarnepensjonAvslagDTO>(),
            Language.Bokmal,
            Fixtures.felles,
        )
        Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter.language, letter.felles, letter.template.letterMetadata.brevtype) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(EtterlatteBrevKode.BARNEPENSJON_AVSLAG.name, it) }
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonAvslag.template,
            Fixtures.create<BarnepensjonAvslagDTO>(),
            Language.Bokmal,
            Fixtures.felles,
        ).let { HTMLDocumentRenderer.render(it) }
            .also { writeTestHTML(EtterlatteBrevKode.BARNEPENSJON_AVSLAG.name, it) }
    }
}
