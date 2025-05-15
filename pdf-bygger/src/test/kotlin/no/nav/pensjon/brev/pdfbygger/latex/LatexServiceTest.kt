package no.nav.pensjon.brev.pdfbygger.latex

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import kotlinx.coroutines.*
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Failure
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse.Success
import no.nav.pensjon.brev.pdfbygger.getScriptPath
import no.nav.pensjon.brev.template.render.DocumentFile
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LatexServiceTest {

    @Test
    fun `producePDF compiles two times`() {
        assertResult<Success>(producePdf("simpleCompile.sh")) { result ->
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
                val service = BlockingLatexService(
                    latexParallelism = 1,
                    queueWaitTimeout = 1.seconds,
                    latexCompileService = LatexCompileService(
                        latexCommand = "/usr/bin/env bash ${getScriptPath("neverEndingCompileWithOutput.sh")} ${output.absolutePathString()}",
                        compileTimeout = 100.milliseconds,
                        tmpBaseDir = null,
                    )
                )
                assertResult<Failure.Timeout>(service.producePDF(emptyList()))

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
        assertResult<Success>(
            producePdf(
                "useFilesCompile.sh", files =
                    listOf(
                        DocumentFile("f1.txt", "file 1"),
                        DocumentFile("f2.txt", "file 2")
                    )
            )
        ) {
            val compiledOutput = it.decodePlaintext().lines()
            assertThat(compiledOutput, hasSize(equalTo(2)) and hasElement("file 1") and hasElement("file 2"))
        }
    }

    @Test
    fun `producePDF fails when command does not exist`() {
        val service = BlockingLatexService(
            latexParallelism = 1,
            queueWaitTimeout = 60.seconds,
            latexCompileService = LatexCompileService(
                latexCommand = "_non_existing.sh",
                compileTimeout = 60.seconds,
                tmpBaseDir = null,
            )
        )
        runBlocking {
            assertResult<Failure.Server>(service.producePDF(emptyList())) {
                assertThat(it.reason, containsSubstring("Compilation process execution failed"))
            }
        }
    }

    @Test
    fun `producePDF trims extra spaces in command`() {
        val service = BlockingLatexService(
            latexParallelism = 1,
            queueWaitTimeout = 60.seconds,
            latexCompileService = LatexCompileService(
                latexCommand = "/usr/bin/env bash ${getScriptPath("simpleCompile.sh")}" + " ",
                compileTimeout = 60.seconds,
                tmpBaseDir = null,
            )
        )
        runBlocking {
            assertResult<Success>(service.producePDF(emptyList()))
        }
    }

    @Test
    fun `producePDF limits parallel compilations`() {
        val service = BlockingLatexService(
            latexParallelism = 1,
            queueWaitTimeout = 100.milliseconds,
            latexCompileService = LatexCompileService(
                latexCommand = "/usr/bin/env bash ${getScriptPath("compileInSeconds.sh")} 0.1" + " ",
                compileTimeout = 500.milliseconds,
                tmpBaseDir = null,
            )
        )

        runBlocking {
            val results = List(10) {
                async {
                    service.producePDF(emptyList())
                }
            }.awaitAll()

            val success = results.filterIsInstance<Success>()
            val timedOut = results.filterIsInstance<Failure.QueueTimeout>()

            // Because of two runs per compilation we expect each to take ~200ms, and queue wait timeout is less, thus ~2 successes
            assertThat(success, hasSize(isWithin(1..3)))
            // The rest we expect to be timeout
            assertThat(timedOut, hasSize(equalTo(results.size - success.size)))
        }
    }

    @Test
    fun `producePDF waits in queue for compilation until max queue wait`() {
        val service = BlockingLatexService(
            latexParallelism = 1,
            queueWaitTimeout = 50.milliseconds,
            latexCompileService = LatexCompileService(
                latexCommand = "/usr/bin/env bash ${getScriptPath("neverEndingCompile.sh")}" + " ",
                compileTimeout = 300.seconds,
                tmpBaseDir = null,
            ),
        )
        runBlocking {
            val blockingCompilation = launch { service.producePDF(emptyList()) }

            delay(10.milliseconds)

            var result: PDFCompilationResponse? = null
            val compilationQueueWait = withTimeoutOrNull(1.seconds) {
                measureTimeMillis {
                    result = service.producePDF(emptyList())
                }
            }

            blockingCompilation.cancel()

            assertNotNull(
                compilationQueueWait,
                "Expected queued compilation to be cancelled by LatexService, but was cancelled by timeout in test"
            )
            assertThat(compilationQueueWait, isWithin(50L..100L))
            assertResult<Failure.QueueTimeout>(result) {
                assertThat(it.reason, containsSubstring("queue wait timed out"))
            }
        }
    }

    @Test
    fun `producePDF waits in compilation queue and will run when admitted before max queue wait timeout`() {
        val service = BlockingLatexService(
            latexParallelism = 1,
            queueWaitTimeout = 1.seconds,
            latexCompileService = LatexCompileService(
                latexCommand = "/usr/bin/env bash ${getScriptPath("compileInSeconds.sh")} 0.1" + " ",
                compileTimeout = 500.milliseconds,
                tmpBaseDir = null,
            )
        )
        runBlocking {
            val blockingCompilation = launch { service.producePDF(emptyList()) }

            delay(10.milliseconds)

            var result: PDFCompilationResponse? = null
            val compilationTime = withTimeoutOrNull(3.seconds) {
                measureTimeMillis { result = service.producePDF(emptyList()) }
            }

            assertNotNull(
                compilationTime,
                "Expected queued compilation to be completed by LatexService, but was cancelled by timeout in test"
            )
            assertThat(compilationTime, isWithin(200L..800L))
            assertResult<Success>(result)

            blockingCompilation.cancel()
        }
    }

    @Test
    fun `LatexService will not enforce any parallelism limit when set to less than 1`() {
        val service = BlockingLatexService(
            latexParallelism = 0,
            queueWaitTimeout = 1.seconds,
            latexCompileService = LatexCompileService(
                latexCommand = "/usr/bin/env bash ${getScriptPath("simpleCompile.sh")}" + " ",
                compileTimeout = 500.milliseconds,
                tmpBaseDir = null,
            )
        )
        runBlocking {
            val requests = List(Runtime.getRuntime().availableProcessors() * 10) {
                async { service.producePDF(emptyList()) }
            }
            val compilationTime = withTimeoutOrNull(10.seconds) {
                measureTimeMillis { requests.awaitAll() }
            }
            assertNotNull(compilationTime, "Test timed out")
            println(compilationTime)
            assertThat(compilationTime, isWithin(1L..1000L))
        }

    }

    private inline fun <reified ToBe : PDFCompilationResponse> assertResult(
        result: PDFCompilationResponse?,
        assertBody: (ToBe) -> Unit = {}
    ) {
        assertIs<ToBe>(result)
        assertBody(result)
    }

    private fun producePdf(
        scriptName: String,
        files: List<DocumentFile> = emptyList(),
        timeout: Duration = 60.seconds
    ): PDFCompilationResponse =
        runBlocking {
            BlockingLatexService(
                queueWaitTimeout = timeout,
                latexParallelism = 1,
                latexCompileService = LatexCompileService(
                    latexCommand = "/usr/bin/env bash ${getScriptPath(scriptName)}",
                    compileTimeout = timeout,
                    tmpBaseDir = null
                )
            ).producePDF(files)
        }

    private fun Success.decodePlaintext(): String = String(this.pdfCompilationOutput.bytes)

}