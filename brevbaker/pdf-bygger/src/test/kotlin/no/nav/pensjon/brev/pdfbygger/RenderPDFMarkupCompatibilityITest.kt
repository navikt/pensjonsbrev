package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.brev.brevbaker.renderTestPDFV2
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestV2
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path

@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class RenderPDFMarkupCompatibilityITest {

    private val pdfCompileService = PdfByggerTestService()

    @ParameterizedTest(name = "{0}")
    @MethodSource("visualTestCases")
    fun `markup v1 and v2 render identical PDFs`(testCase: RenderPDFVisualTestCase) {
        val (v1, v2) = runBlocking {
            coroutineScope {
                val v1 = async { renderV1(testCase) }
                val v2 = async { renderV2(testCase) }
                v1.await() to v2.await()
            }
        }

        assertArrayEquals(
            v1.withStablePdfMetadata(),
            v2.withStablePdfMetadata(),
            "PDF from markup v1 and v2 differ for '${testCase.testName}'",
        )
    }

    private fun renderV1(testCase: RenderPDFVisualTestCase): ByteArray =
        CapturingPdfByggerService(pdfCompileService)
            .also {
                testCase.letter().renderTestPDF(
                    pdfFileName = testCase.testName,
                    path = Path.of("build/test_visual/pdf-markup-compatibility/v1"),
                    pdfByggerService = it,
                )
            }.lastPdfBytes()

    private fun renderV2(testCase: RenderPDFVisualTestCase): ByteArray =
        CapturingPdfByggerService(pdfCompileService)
            .also {
                testCase.letter().renderTestPDFV2(
                    pdfFileName = testCase.testName,
                    path = Path.of("build/test_visual/pdf-markup-compatibility/v2"),
                    pdfByggerService = it,
                )
            }.lastPdfBytes()

    private class CapturingPdfByggerService(private val delegate: PdfByggerTestService) : PDFByggerService {
        private var lastPdf: ByteArray? = null

        override suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput =
            delegate.producePDF(pdfRequest).also { lastPdf = it.bytes }

        override suspend fun producePDFV2(pdfRequest: PDFRequestV2): PDFCompilationOutput =
            delegate.producePDFV2(pdfRequest).also { lastPdf = it.bytes }

        fun lastPdfBytes(): ByteArray =
            requireNotNull(lastPdf) { "PDF was not produced" }
    }

    companion object {
        private val xmpMetadata = Regex("""(?s)<\?xpacket begin=.*?<\?xpacket end="r"\?>""")
        private val pdfInfoDate = Regex("""/(ModDate|CreationDate) \(D:[^)]+\)""")
        private val trailerId = Regex("""/ID \[[^]]+]""")

        @JvmStatic
        fun visualTestCases(): List<RenderPDFVisualTestCase> = RenderPDFVisualTestCases.allCases

        private fun ByteArray.withStablePdfMetadata(): ByteArray =
            toString(Charsets.ISO_8859_1)
                .replace(xmpMetadata, "<xpacket ignored />")
                .replace(pdfInfoDate) { "/${it.groupValues[1]} (<ignored>)" }
                .replace(trailerId, "/ID [<ignored>]")
                .toByteArray(Charsets.ISO_8859_1)
    }
}
