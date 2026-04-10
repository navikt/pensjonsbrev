package no.nav.pensjon.brev.pdfbygger.latex

import kotlinx.coroutines.runBlocking
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.*
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.pdfbygger.letterMarkup
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(InterneDataklasser::class)
@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class PensjonLatexITest {
    private val pdfCompileService = PdfByggerTestService()

    @Test
    fun canRender() {
        val template = createTemplate(
            letterDataType = EmptyAutobrevdata::class,
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin display tittel",
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
        LetterImpl(template, EmptyAutobrevdata, Bokmal, FellesFactory.felles).renderTestPDF("pensjonLatexITest_canRender", pdfByggerService = pdfCompileService)
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { pdfCompileService.ping() }
    }

    @Test
    fun `title with latex code synthax should not fail compilation`(){
        val template = createTemplate(
            letterDataType = EmptyAutobrevdata::class,
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin display tittel",
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        ) {
            title { text(bokmal { +"En fin tittel med masse LaTeX kommando tegn \$%&\\^_{}~" }) }
            outline {}
        }
        LetterImpl(template, EmptyAutobrevdata, Bokmal, FellesFactory.felles).renderTestPDF("pensjonLatexITest_escape_xmp_title", pdfByggerService = pdfCompileService)

    }


    @Test
    fun `all supported characters render in a single PDF`() {
        val allSupported = (1..Char.MAX_VALUE.code)
            .filter { code -> !Char(code).isSurrogate() } // surrogate's er UTF-16 som vi ikke serialiserer uansett.

        // Split into lines of 64 characters for readability in the PDF
        val lines = allSupported.chunked(64) { chunk ->
            chunk.joinToString(" ") { Char(it).toString() }
        }

        val markup = letterMarkup {
            title { text("Character support smoke test") }
            outline {
                lines.forEach { line ->
                    paragraph { text(line) }
                }
            }
        }

        val result = runBlocking {
            pdfCompileService.producePDF(
                PDFRequest(
                    letterMarkup = markup,
                    attachments = emptyList(),
                    language = LanguageCode.BOKMAL,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
                shouldRetry = false,
                useTypst = true
            )
        }

        writeTestPDF("all_supported_characters_smoke_test", result.bytes)
        println("Smoke test passed: rendered ${allSupported.size} characters in a single PDF.")
    }

    @ParameterizedTest
    @MethodSource("allCharacterRanges")
    fun `try different characters to attempt escaping`(fromRange: Int, toRange: Int) {
        val unsupportedCodePoints = ArrayList<Int>()

        findUnsupportedCharacters(fromRange, toRange, unsupportedCodePoints)

        val summary = buildString {
            appendLine()
            appendLine("=== CHARACTER ESCAPE TEST SUMMARY ===")
            appendLine("Range tested: $fromRange..$toRange")
            if (unsupportedCodePoints.isEmpty()) {
                appendLine("All characters in range rendered successfully.")
            } else {
                appendLine("Found ${unsupportedCodePoints.size} unsupported character(s):")
                // Group consecutive code points into ranges for compact output
                val ranges = mutableListOf<IntRange>()
                for (code in unsupportedCodePoints.sorted()) {
                    val last = ranges.lastOrNull()
                    if (last != null && code == last.last + 1) {
                        ranges[ranges.lastIndex] = last.first..code
                    } else {
                        ranges.add(code..code)
                    }
                }
                ranges.forEach { range ->
                    if (range.first == range.last) {
                        val hex = "U+${range.first.toString(16).uppercase().padStart(4, '0')}"
                        val display = describeChar(range.first)
                        appendLine("    - $hex (code=${range.first}) $display")
                    } else {
                        val hexFirst = "U+${range.first.toString(16).uppercase().padStart(4, '0')}"
                        val hexLast = "U+${range.last.toString(16).uppercase().padStart(4, '0')}"
                        appendLine("    - $hexFirst..$hexLast (code=${range.first}..${range.last}, ${range.last - range.first + 1} chars)")
                    }
                }
            }
            appendLine()
            appendLine("=== END SUMMARY ===")
        }
        println(summary)
    }

    private fun describeChar(code: Int): String {
        val char = Char(code)
        return when {
            char.isISOControl() -> "<control>"
            char.isWhitespace() -> "<whitespace>"
            !char.isDefined() -> "<undefined>"
            else -> "'$char'"
        }
    }

    private fun findUnsupportedCharacters(begin: Int, end: Int, unsupportedCodePoints: ArrayList<Int>) {
        val work = ArrayDeque<Pair<Int, Int>>()
        work.addLast(begin to end)

        while (work.isNotEmpty()) {
            val (lo, hi) = work.removeLast()
            val validCodePoints = (lo..hi).filter { isValidCodePoint(it) }
            if (validCodePoints.isEmpty()) continue

            if (testCharacters(validCodePoints)) continue

            if (validCodePoints.size == 1) {
                unsupportedCodePoints.add(validCodePoints.single())
                continue
            }

            val mid = lo + (hi - lo) / 2
            work.addLast(mid + 1 to hi)
            work.addLast(lo to mid)
        }
    }

    // Surrogates and null cannot arrive via JSON/HTTP, so skip them.
    // Characters in CHARACTER_BLOCKLIST are already stripped by the escape logic, so skip those too.
    private fun isValidCodePoint(code: Int): Boolean {
        if (Char(code).isSurrogate()) return false
        if (code == 0) return false
        return true
    }

    private fun testCharacters(codePoints: List<Int>): Boolean {
        val testString = codePoints.joinToString(" ") { Char(it).toString() }
        return try {
            val markup = letterMarkup {
                title { text("En fin tittel") }
                outline {
                    paragraph {
                        text(testString + "test")
                    }
                }
            }

            runBlocking {
                pdfCompileService.producePDF(
                    PDFRequest(
                        letterMarkup = markup,
                        attachments = emptyList(),
                        language = LanguageCode.BOKMAL,
                        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                    ),
                    shouldRetry = false,
                    useTypst = true,
                )
            }

            true
        } catch (_: Throwable) {
            false
        }
    }


    companion object {
        @JvmStatic
        fun allCharacterRanges(): List<Arguments> {
            val parts = 16
            val partSize = (Char.MAX_VALUE.code + 1) / parts
            return List(parts) {
                val start = it * partSize
                val end = ((it + 1) * partSize - 1).coerceAtMost(Char.MAX_VALUE.code)
                Arguments.of(start, end)
            }
        }
    }

}