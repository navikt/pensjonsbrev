package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.runBlocking
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
class RenderPDFVisualITest {

    private val pdfCompileService = PdfByggerTestService()

    @ParameterizedTest(name = "{0}")
    @MethodSource("visualTestCases")
    fun `render visual PDF`(testCase: RenderPDFVisualTestCase) {
        val pdf = runBlocking { pdfCompileService.producePDFV2(testCase.v2()) }
        writeTestPDF(
            pdfFileName = testCase.testName,
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }

    companion object {
        @JvmStatic
        fun visualTestCases(): List<RenderPDFVisualTestCase> = RenderPDFVisualTestCases.allCases
    }
}
