package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.brev.brevbaker.LaTeXCompilerService
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PensjonLatexITest {
    private val logger = LoggerFactory.getLogger(PensjonLatexITest::class.java)
    private val brevData = TestTemplateDto("Ole")

    private fun latexCompilerService() = LaTeXCompilerService(PDFByggerTestContainer.mappedUrl())

    @Test
    fun canRender() {
        val template = createTemplate(
            letterDataType = TestTemplateDto::class,
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin display tittel",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        ) {
            title { text(bokmal { +"En fin tittel" }) }
            outline {
                paragraph {
                    text(bokmal { +"Argumentet etNavn er: " })
                    eval(etNavn)
                }
            }
        }
        LetterImpl(template, brevData, Bokmal, FellesFactory.felles).renderTestPDF("pensjonLatexITest_canRender", pdfByggerService = latexCompilerService())
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { latexCompilerService().ping() }
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
                letterDataType = TestTemplateDto::class,
                languages = languages(Bokmal),
                letterMetadata = LetterMetadata(
                    displayTitle = "En fin display tittel",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                )
            ) {
                title { text(bokmal { +"En fin tittel" }) }
                outline {
                    paragraph {
                        text(bokmal { +addChars(startChar, endChar) + "test" })
                        eval(etNavn)
                    }
                }
            }

            LetterImpl(testTemplate, brevData, Bokmal, FellesFactory.felles)
                .renderTestPDF("LATEX_ESCAPE_TEST_$startChar-$endChar", pdfByggerService = latexCompilerService())

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