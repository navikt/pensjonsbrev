package no.nav.pensjon.brev.pdfbygger

import java.nio.file.Path

/**
 * Test-tags used by pdf-bygger's own integration/manual tests. Intentionally a pdf-bygger-local copy
 * so the module's tests do not depend on `brevbaker:core`/`brevbaker:dsl` test fixtures.
 *
 * `PdfByggerTestService`/`PDFByggerTestContainer` er derimot delt, og bor i
 * `brevbaker:jackson` sine testFixtures.
 */
object TestTags {
    const val INTEGRATION_TEST = "integration-test"

    // For visual inspection of documents/design
    const val MANUAL_TEST = "manual-test"
}

/**
 * Writes a rendered PDF to disk for local/visual inspection.
 */
fun writeTestPDF(pdfFileName: String, pdf: ByteArray, path: Path = Path.of("build", "test_pdf")) {
    val file = path.resolve("${pdfFileName.replace(" ", "_")}.pdf").toFile()
    file.parentFile.mkdirs()
    file.writeBytes(pdf)
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}
