package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.PDF_BUILDER_URL
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.render.TestTemplateDtoSelectors.etNavn
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.opentest4j.AssertionFailedError
import org.slf4j.LoggerFactory

data class TestTemplateDto(val etNavn: String)

@TemplateModelHelpers
object Helpers : HasModel<TestTemplateDto>

private const val FIND_FAILING_CHARACTERS = false

@Tag(TestTags.INTEGRATION_TEST)
class PensjonLatexITest {
    private val logger = LoggerFactory.getLogger(PensjonLatexITest::class.java)
    private val brevData = TestTemplateDto("Ole")

    private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)

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
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        ) {
            title { text(Bokmal to "En fin tittel") }
            outline {
                paragraph {
                    text(Bokmal to "Argumentet etNavn er: ")
                    eval(etNavn)
                }
            }
        }
        Letter(template, brevData, Bokmal, Fixtures.felles).renderTestPDF("pensjonLatexITest_canRender", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).ping() }
    }

    // To figure out which character makes the compilation fail, set the FIND_FAILING_CHARACTERS to true.
    // FIND_FAILING_CHARACTERS is disabled by default to not take up too much time in case of universally failing compilation.
    @ParameterizedTest
    @MethodSource("allCharacterRanges")
    fun `try different characters to attempt escaping LaTeX`(fromRange: Int, toRange: Int) {
        //allCharacterRanges
        val invalidCharacters = ArrayList<Int>()

        // split in multiple parts so that it doesn't time out the letter compilation

        isValidCharacters(fromRange, toRange, invalidCharacters)

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
        } else if (FIND_FAILING_CHARACTERS) {
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
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                )
            ) {
                title { text(Bokmal to "En fin tittel") }
                outline {
                    paragraph {
                        text(Bokmal to addChars(startChar, endChar) + "test")
                        eval(etNavn)
                    }
                }
            }

            Letter(testTemplate, brevData, Bokmal, Fixtures.felles)
                .renderTestPDF("LATEX_ESCAPE_TEST_$startChar-$endChar", pdfByggerService = laTeXCompilerService)

            return true
        } catch (e: Throwable) {
            if (!FIND_FAILING_CHARACTERS) throw e
            else logger.error("Failed printing character in range $startChar - $endChar with message: ${e.message}")
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

    companion object {
        @JvmStatic
        fun allCharacterRanges(): List<Arguments> {
            val parts = 4
            val partSize = Char.MAX_VALUE.code / parts
            return List(parts){
                Arguments.of((it * partSize), (((it + 1) * partSize + it).coerceAtMost(Char.MAX_VALUE.code)))
            }
        }
    }

}