package no.nav.pensjon.etterlatte.maler.barnepensjon

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
import no.nav.pensjon.etterlatte.Fixtures
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class BarnepensjonInnvilgelseTest {

    @Test
    fun pdftest() {
        Letter(
            BarnepensjonInnvilgelse.template,
            Fixtures.create<BarnepensjonInnvilgelseDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("BARNEPENSJON_INNVILGELSE", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonInnvilgelse.template,
            Fixtures.create<BarnepensjonInnvilgelseDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("BARNEPENSJON_INNVILGELSE", it) }
    }

}