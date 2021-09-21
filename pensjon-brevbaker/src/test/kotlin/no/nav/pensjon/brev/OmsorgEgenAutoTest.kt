package no.nav.pensjon.brev

import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class OmsorgEgenAutoTest {

//    @Disabled
    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            OmsorgEgenAutoDto(arEgenerklaringOmsorgspoeng = 2020, arInnvilgetOmsorgspoeng = 2021),
            Language.Bokmal,
            Fixtures.felles.copy(signerendeSaksbehandlere = null)
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .also { File("test-params.tex").writeBytes(Base64.getDecoder().decode(it.files["letter.tex"])) }
//            .also { File("test-params.tex").writeBytes(Base64.getDecoder().decode(it.files["params.tex"])) }
            .let { LaTeXCompilerService().producePDF(it) }
            .also { File("test.pdf").writeBytes(Base64.getDecoder().decode(it)) }
    }

}