package no.nav.pensjon.etterlatte.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brev.writeTestHTML
import no.nav.pensjon.brev.writeTestPDF
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelse
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.PDF_BYGGER)
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