package no.nav.pensjon.etterlatte.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.etterlatte.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class EtterlatteBrevTest {

    @Test
    fun pdftest() {
        Letter(
            EtterlatteBrev.template,
            EtterlatteBrevDto("Test Testeson"),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("ETTERLATTE_BREV", it) }
    }

}