package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        val test = UfoerOmregningEnsligDto().copy(
            HAR_MINSTEYT_VEDVIRK = false,
                    INNTEKT_UFOERE_ENDRET = true,
                    EKTEFELLETILLEGG_OPPHOERT = true,
                    HAR_BARNETILLEGG_FOR_SAERKULLSBARN = true,
                    BARN_OVERFOERT_TIL_SAERKULLSBARN = true,
        )
        Letter(
            UfoerOmregningEnslig.template,
            test,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it).base64PDF } }
            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}