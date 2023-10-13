package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import io.ktor.util.*
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Base64PDF
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Failure
import org.slf4j.LoggerFactory
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LatexServiceTest {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `producePDF compiles two times`() {
        assertResult<Base64PDF>(producePdf("simpleCompile.sh")) { result ->
            val compiledOutput = result.decodePlaintext().lines().filter { it.isNotBlank() }
            assertThat(compiledOutput, hasSize(equalTo(2)))
            assertThat(compiledOutput, allElements(equalTo("kompilerer letter.tex")))
        }
    }

    @Test
    fun `producePDF aborts subsequent compilations if first fails`() {
        assertResult<Failure.Client>(producePdf("failingCompile.sh")) {
            // Should only be one error message, if two then the compilation ran twice
            assertEquals("feiler", it.error?.trim())
        }
    }

    @Test
    fun `producePDF returns failure if last compile fails`() {
        assertResult<Failure.Client>(producePdf("failingSecondCompile.sh")) {
            assertEquals("feilet letter.tex", it.error?.trim())
            assertEquals("kompilerer letter.tex", it.output?.trim())
        }
    }

    @Test
    fun `producePDF times out`() {
        assertResult<Failure.Timeout>(producePdf("neverEndingCompile.sh", timeout = 10.milliseconds)) {
            assertThat(it.reason, containsSubstring("10 MILLISECONDS"))
        }
    }

    @Test
    fun `producePDF writes all inputFiles`() {
        assertResult<Base64PDF>(producePdf("useFilesCompile.sh", files = mapOf("f1.txt" to "file 1".encodeBase64(), "f2.txt" to "file 2".encodeBase64()))) {
            val compiledOutput = it.decodePlaintext().lines()
            assertThat(compiledOutput, hasSize(equalTo(2)) and hasElement("file 1") and hasElement("file 2"))
        }
    }

    @Test
    fun `producePDF fails when command does not exist`() {
        val service = LaTeXService(logger, latexCommand = "_non_existing.sh")
        assertResult<Failure.Server>(service.producePDF(emptyMap())) {
            assertThat(it.reason, containsSubstring("Compilation process execution failed"))
        }
    }

    private inline fun <reified ToBe: PDFCompilationResponse> assertResult(result: PDFCompilationResponse, assertBody: (ToBe) -> Unit) {
        assertIs<ToBe>(result)
        assertBody(result)
    }

    private fun getScriptPath(name: String): String = this::class.java.classLoader.getResource(name)!!.path

    private fun producePdf(scriptName: String, files: Map<String, String> = emptyMap(), timeout: Duration = 60.seconds): PDFCompilationResponse =
        LaTeXService(logger, latexCommand = "/usr/bin/env bash ${getScriptPath(scriptName)}", timeout = timeout).producePDF(files)

    private fun Base64PDF.decodePlaintext(): String =
        base64PDF.decodeBase64String()

}