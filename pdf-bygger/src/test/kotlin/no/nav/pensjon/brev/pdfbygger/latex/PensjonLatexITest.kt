package no.nav.pensjon.brev.pdfbygger.latex

import kotlinx.coroutines.runBlocking
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LaTeXCompilerService
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.opentest4j.AssertionFailedError
import org.slf4j.LoggerFactory

private const val FIND_FAILING_CHARACTERS = false

@OptIn(InterneDataklasser::class)
@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PensjonLatexITest {
    private val logger = LoggerFactory.getLogger(PensjonLatexITest::class.java)

    private val laTeXCompilerService = LaTeXCompilerService(PDFByggerTestContainer.mappedUrl())

    @Test
    fun canRender() {
        val template = createTemplate(
            letterDataType = EmptyBrevdata::class,
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
                    text(bokmal { +"Argumentet etNavn er: " }) }
            }
        }
        LetterImpl(template, EmptyBrevdata, Bokmal, FellesFactory.felles).renderTestPDF("pensjonLatexITest_canRender", pdfByggerService = laTeXCompilerService)
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { laTeXCompilerService.ping() }
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
        assertThat(invalidCharacters).isEmpty()
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
                letterDataType = EmptyBrevdata::class,
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
                    }
                }
            }

            LetterImpl(testTemplate, EmptyBrevdata, Bokmal, FellesFactory.felles)
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