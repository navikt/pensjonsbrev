package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

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
                val v1 = async { pdfCompileService.producePDF(testCase.v1()).bytes }
                val v2 = async { pdfCompileService.producePDFV2(testCase.v2()).bytes }
                v1.await() to v2.await()
            }
        }

        assertArrayEquals(
            v1.withStablePdfMetadata(),
            v2.withStablePdfMetadata(),
            "PDF from markup v1 and v2 differ for '${testCase.testName}'",
        )
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
