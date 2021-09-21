package no.nav.pensjon.brev.template.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.LetterRequest
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

data class TestTemplateDto(val etNavn: String)

@Tag(TestTags.PDF_BYGGER)
class PensjonLatexITest {

    val brevData = TestTemplateDto("Ole")

    val template = createTemplate(
        name = "test-template",
        base = PensjonLatex,
        parameterType = TestTemplateDto::class,
        lang = languages(Language.Bokmal),
        title = newText(Language.Bokmal to "En fin tittel"),
    ) {
        outline {
            text(Language.Bokmal to "Argumentet etNavn er: ")
            selectField(TestTemplateDto::etNavn)
            newline()
        }
    }

    @Test
    fun canRender() {
        Letter(template, brevData, Language.Bokmal, Fixtures.felles)
            .render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it) }
            .let { Base64.getDecoder().decode(it) }
//            .also { File("test.pdf").writeBytes(it) }

    }

}