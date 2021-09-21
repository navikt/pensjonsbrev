package no.nav.pensjon.brev.something

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.pensjon.brev.api.LetterRequest
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.felles
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

@Disabled //pdf compilation integration test
internal class PensjonLatexTest {

    val omsorgEgenAutoDto = ObjectMapper().convertValue(
        OmsorgEgenAutoDto(
            arEgenerklaringOmsorgspoeng = 2020,
            arInnvilgetOmsorgspoeng = 2021,
        ), ObjectNode::class.java
    )

    @Test
    fun render() {
        LetterRequest(OmsorgEgenAuto.template.name, omsorgEgenAutoDto, felles, Language.Bokmal)
            .let { LetterResource.create(it) }
            .render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it) }
            .let { Base64.getDecoder().decode(it) }
            .let { File("test.pdf").writeBytes(it) }
    }

}