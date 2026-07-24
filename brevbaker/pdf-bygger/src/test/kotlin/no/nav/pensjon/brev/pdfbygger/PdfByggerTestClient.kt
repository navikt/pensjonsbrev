package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import kotlinx.serialization.json.Json
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.PullPolicy
import org.testcontainers.utility.DockerImageName
import java.nio.file.Path
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Test-tags used by pdf-bygger's own integration/manual tests. Intentionally a pdf-bygger-local copy
 * so the module's tests do not depend on `brevbaker:core`/`brevbaker:dsl` test fixtures.
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

/**
 * HTTP client for pdf-bygger's own integration tests. Posts markup/PDF requests directly to the
 * running pdf-bygger container and returns the compiled PDF, without going through `brevbaker:core`.
 */
class PdfByggerTestService(
    private val pdfByggerUrl: String = PDFByggerTestContainer.mappedUrl(),
    private val logWarning: (String) -> Unit = ::println,
) {
    private val objectmapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    logWarning("pdf-bygger returnerte ${response.status}: ${response.bodyAsText()}")
                }
            }
        }

        engine {
            requestTimeout = 0
        }
    }

    suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(objectmapper.writeValueAsBytes(pdfRequest))
        }.body()

    suspend fun producePDFV2(pdfRequest: LetterPDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/v2/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(LetterPDFRequest.serializer(), pdfRequest))
        }.body()

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}

/**
 * Testcontainer for the pdf-bygger image. pdf-bygger-local copy of the equivalent core test fixture.
 */
object PDFByggerTestContainer {

    // Sett miljøvariabel BRUK_LOKAL_PDF_BYGGER=true for å kjøre testene lokalt mot din nyest bygde pdf-bygger.
    private val BRUK_LOKAL_PDF_BYGGER = System.getenv("BRUK_LOKAL_PDF_BYGGER")?.toBoolean() == true

    // Sett miljøvariabel TESTCONTAINERS_REUSE_ENABLE=true for å gjenbruke pdf-bygger containeren mellom tester.
    // Om du bruker denne lokalt, husk å stopp kjørende testcontainer for å oppdatere docker imaget.
    private val REUSE_CONTAINER = System.getenv("TESTCONTAINERS_REUSE_ENABLE")?.toBoolean() == true

    private val pdfContainer: GenericContainer<*> = konfigurerPdfbyggerContainer()

    private const val PORT = 8080

    private fun konfigurerPdfbyggerContainer(): GenericContainer<*> {
        // PDF_BYGGER_IMAGE blir i GitHub Actions-byggejobbane sendt inn som miljøvariabel
        val envImageName = System.getenv("PDF_BYGGER_IMAGE")?.takeIf { it.isNotBlank() }
        val fullImageName = when {
            envImageName != null -> envImageName
            BRUK_LOKAL_PDF_BYGGER -> "pensjonsbrev-pdf-bygger:latest"
            else -> "ghcr.io/navikt/pensjonsbrev/pdf-bygger:main"
        }
        val pullPolicy = if (envImageName == null && BRUK_LOKAL_PDF_BYGGER) PullPolicy.defaultPolicy() else PullPolicy.alwaysPull()
        return GenericContainer(DockerImageName.parse(fullImageName))
            .withImagePullPolicy(pullPolicy)
            .withExposedPorts(PORT)
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("pdf-bygger")))
            .withEnv(
                "JAVA_TOOL_OPTIONS",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork"
            )
            // bakover-ompatibilitet med pdf-bygger (brukt i integrasjonstester i brevbaker på github actions).
            .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
            .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
            .waitingFor(
                Wait.forHttp("/isReady")
                    .forStatusCode(200)
                    .withStartupTimeout(50.seconds.toJavaDuration())
            )
            .withReuse(REUSE_CONTAINER)
    }

    fun mappedUrl(): String {
        start()
        @Suppress("HttpUrlsUsage") // Kun for lokal kjøring
        return "http://${pdfContainer.host}:${pdfContainer.getMappedPort(PORT)}"
    }

    @Synchronized
    private fun start() {
        if (!pdfContainer.isRunning) {
            pdfContainer.start()
        }
    }
}
