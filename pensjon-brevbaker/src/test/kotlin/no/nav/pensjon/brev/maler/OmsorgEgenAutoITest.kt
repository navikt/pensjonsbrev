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

@Tag(TestTags.PDF_BYGGER)
class OmsorgEgenAutoITest {

    @Test
    fun `end to end test`() {
        requestLetter(
            LetterRequest(
                "OMSORG_EGEN_AUTO",
                OmsorgEgenAutoDto(),
                felles = Fixtures.felles,
                LanguageCode.ENGLISH
            )
        ).also { writeTestPDF("000104-english" ,it.base64pdf) }
    }
}