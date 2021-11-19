package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class OmsorgEgenAutoITest {
    @Tag(TestTags.PDF_BYGGER)
    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            OmsorgEgenAutoDto(),
            Language.English,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it).base64PDF }
            .also { writeTestPDF("000104-english" ,it) }
    }

    @Tag(TestTags.PDF_BYGGER)
    @Test
    fun `end to end test`() {
        requestLetter(
            LetterRequest(
                "OMSORG_EGEN_AUTO",
                OmsorgEgenAutoDto(),
                felles = Fixtures.felles,
                LanguageCode.BOKMAL
            )
        )

    }
}