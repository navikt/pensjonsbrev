package no.nav.brev.brevbaker

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.PullPolicy
import org.testcontainers.utility.DockerImageName

object PDFByggerTestContainer {

    private val pdfContainer: GenericContainer<*> = konfigurerPdfbyggerContainer()

    // Overstyr denne hvis du vil kjøre testene lokalt mot din nyest bygde pdf-bygger
    // TODO: Endre frå true til false når denne PR-en er merga til main
    private const val BRUK_LOKAL_CONTAINER = true

    private const val PORT = 8080

    private fun konfigurerPdfbyggerContainer(): GenericContainer<*> {
        // PDF_BYGGER_IMAGE blir i GitHub Actions-byggejobbane sendt inn som miljøvariabel
        val envImageName = System.getenv("PDF_BYGGER_IMAGE")?.takeIf { it.isNotBlank() }
        val fullImageName = when {
            envImageName != null -> envImageName
            BRUK_LOKAL_CONTAINER -> "pensjonsbrev-pdf-bygger:latest"
            else -> "ghcr.io/navikt/pensjonsbrev/pdf-bygger:main"
        }
        return GenericContainer(DockerImageName.parse(fullImageName))
            .withImagePullPolicy(PullPolicy.alwaysPull())
            .withExposedPorts(PORT)
            .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
            .withEnv(
                "JAVA_TOOL_OPTIONS",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork"
            )
            .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
            .waitingFor(Wait.forHttp("/isReady").forStatusCode(200))
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
