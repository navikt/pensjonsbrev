package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.BarnepensjonVedtakDTO
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.PDF_BYGGER)
class BarnepensjonVedtakITest {

    @Test
    fun pdftest() {
        Letter(
            BarnepensjonVedtak.template,
            Fixtures.create<BarnepensjonVedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("BARNEPENSJON_VEDTAK", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonVedtak.template,
            Fixtures.create<BarnepensjonVedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("BARNEPENSJON_VEDTAK", it) }
    }

}