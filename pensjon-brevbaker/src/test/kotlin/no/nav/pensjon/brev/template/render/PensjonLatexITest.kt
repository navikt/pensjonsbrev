package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.render.TestTemplateDtoSelectors.etNavn
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

data class TestTemplateDto(val etNavn: String)
@TemplateModelHelpers
object Helpers : HasModel<TestTemplateDto>

@Tag(TestTags.PDF_BYGGER)
class PensjonLatexITest {

    private val brevData = TestTemplateDto("Ole")
    @Test
    fun canRender() {
        val template = createTemplate(
            name = "test-template",
            letterDataType = TestTemplateDto::class,
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin display tittel",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            )
        ) {
            title { text(Bokmal to "En fin tittel") }
            outline {
                text(Bokmal to "Argumentet etNavn er: ")
                eval(etNavn)
            }
        }
        Letter(template, brevData, Bokmal, Fixtures.felles)
            .let { PensjonLatexRenderer.render(it) }
            .let { LaTeXCompilerService(PDF_BUILDER_URL).producePdfSync(it).base64PDF }
            .also { writeTestPDF("pensjonLatexITest_canRender", it) }
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).ping() }
    }

    @Test
    fun `try different characters to attempt escaping LaTeX`() {
        val invalidCharacters = ArrayList<Int>()
        isValidCharacters(0, Char.MAX_VALUE.code, invalidCharacters)
        if (invalidCharacters.isNotEmpty()) {
            throw AssertionFailedError(
                """
                    Escaped characters managed to crash the letter compilation:
                    ${invalidCharacters.joinToString()}}
                """.trimIndent()
            )
        }
        assertThat(invalidCharacters, isEmpty)


    }

    private fun isValidCharacters(begin: Int, end: Int, invalidCharacters: ArrayList<Int>) {
        if (testCharacters(begin, end)) {
            //All characters are valid
            return
        } else {
            if (begin - end == 0) {
                //Failed at single character
                invalidCharacters.add(begin)
                return
            }
            // there is some invalid character in the range
            val separationPoint = begin + ((end - begin) / 2)
            isValidCharacters(begin, separationPoint, invalidCharacters)
            isValidCharacters(separationPoint + 1, end, invalidCharacters)
            return
        }
    }

    private fun testCharacters(startChar: Int, endChar: Int): Boolean {
        try {
            val testTemplate = createTemplate(
                name = "test-template",
                letterDataType = TestTemplateDto::class,
                languages = languages(Bokmal),
                letterMetadata = LetterMetadata(
                    displayTitle = "En fin display tittel",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                )
            ) {
                title { text(Bokmal to "En fin tittel") }
                outline {
                    text(Bokmal to addChars(startChar, endChar) + "test")
                    eval(etNavn)
                }
            }

            Letter(testTemplate, brevData, Bokmal, Fixtures.felles)
                .let { PensjonLatexRenderer.render(it) }
                .let { LaTeXCompilerService(PDF_BUILDER_URL).producePdfSync(it).base64PDF }
                .also { writeTestPDF("LATEX_ESCAPE_TEST_$startChar-$endChar", it) }

            return true
        } catch (e: Throwable) {
            return false
        }
    }

    private fun addChars(from: Int, to: Int): String {
        val stringBuilder = StringBuilder()
        for (i in from..to) {
            stringBuilder.append(Char(i)).append(" ")
        }
        return stringBuilder.toString()
    }
}