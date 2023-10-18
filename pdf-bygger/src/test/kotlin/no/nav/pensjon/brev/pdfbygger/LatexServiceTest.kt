package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import io.ktor.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Base64PDF
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Failure
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LatexServiceTest {

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
    fun `producePDF times out when first execution exceeds timout`() {
        assertResult<Failure.Timeout>(producePdf("neverEndingCompile.sh", timeout = 10.milliseconds)) {
            assertThat(it.reason, containsSubstring(10.milliseconds.toString()))
        }
    }

    @Test
    fun `producePDF times out when subsequent executions exceeds timeout`() {
        assertResult<Failure.Timeout>(producePdf("100msCompile.sh", timeout = 150.milliseconds)) {
            assertThat(it.reason, containsSubstring(150.milliseconds.toString()))
        }
    }

    @Test
    fun `producePDF does not leave dangling compile process on timeout`() {
        val output = createTempFile()
        try {
            runBlocking {
                val service = LaTeXService(
                    latexCommand = "/usr/bin/env bash ${getScriptPath("neverEndingCompileWithOutput.sh")} ${output.absolutePathString()}",
                    latexParallelism = 1,
                    timeout = 100.milliseconds,
                )
                assertResult<Failure.Timeout>(service.producePDF(emptyMap()))

                val contentAfterTimeout = output.readText()
                delay(500.milliseconds)
                val contentAfterDelay = output.readText()

                assertEquals(contentAfterTimeout.length, contentAfterDelay.length)
                assertEquals(contentAfterTimeout, contentAfterDelay)
            }
        } finally {
            output.deleteIfExists()
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
        val service = LaTeXService(latexCommand = "_non_existing.sh", latexParallelism = 1, timeout = 60.seconds)
        runBlocking {
            assertResult<Failure.Server>(service.producePDF(emptyMap())) {
                assertThat(it.reason, containsSubstring("Compilation process execution failed"))
            }
        }
    }

    @Test
    fun `producePDF trims extra spaces in command`() {
        val service = LaTeXService(latexCommand = "/usr/bin/env bash ${getScriptPath("simpleCompile.sh")}" + " ", latexParallelism = 1, timeout = 60.seconds)
        runBlocking {
            assertResult<Base64PDF>(service.producePDF(emptyMap()))
        }
    }

    @Test
    fun `producePDF limits parallel compilations`() {
        val service = LaTeXService(latexCommand = "/usr/bin/env bash ${getScriptPath("compileInSeconds.sh")} 0.1" + " ", latexParallelism = 1, timeout = 500.milliseconds)
        runBlocking {
            val results = (0..10).map {
                async {
                    service.producePDF(emptyMap())
                }
            }.awaitAll()

            val success = results.filterIsInstance<Base64PDF>()
            val timedOut = results.filterIsInstance<Failure.Timeout>()

            assertThat(success, hasSize(isWithin(1..3)))
            assertThat(timedOut, hasSize(equalTo(results.size - success.size)))
        }
    }

    private inline fun <reified ToBe : PDFCompilationResponse> assertResult(result: PDFCompilationResponse, assertBody: (ToBe) -> Unit = {}) {
        assertIs<ToBe>(result)
        assertBody(result)
    }

    private fun producePdf(scriptName: String, files: Map<String, String> = emptyMap(), timeout: Duration = 60.seconds): PDFCompilationResponse =
        runBlocking {
            LaTeXService(latexCommand = "/usr/bin/env bash ${getScriptPath(scriptName)}", timeout = timeout, latexParallelism = 1).producePDF(files)
        }

    private fun Base64PDF.decodePlaintext(): String =
        base64PDF.decodeBase64String()

}