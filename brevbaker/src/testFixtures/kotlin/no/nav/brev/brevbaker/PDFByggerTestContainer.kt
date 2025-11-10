package no.nav.brev.brevbaker

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object PDFByggerTestContainer {

    private val pdfContainer: GenericContainer<*> = konfigurerPdfbyggerContainer()

    // Overstyr denne hvis du vil kjøre testene lokalt mot din nyest bygde pdf-bygger
    // TODO: Endre frå true til false når denne PR-en er merga til main
    private const val BRUK_LOKAL_CONTAINER = true

    private fun konfigurerPdfbyggerContainer(): GenericContainer<*> {
        // DIGEST blir i GitHub Actions-byggejobbane sendt inn som miljøvariabel
        val fullImageName = System.getenv("PDF_BYGGER_DIGEST")
            ?.takeIf { it.isNotBlank() }
            ?.let { "ghcr.io/navikt/pensjonsbrev/pdf-bygger:$it" }
            ?: if (BRUK_LOKAL_CONTAINER) "pensjonsbrev-pdf-bygger:latest" else "ghcr.io/navikt/pensjonsbrev/pdf-bygger:main"
        return GenericContainer(DockerImageName.parse(fullImageName))
            .withExposedPorts(8080)
            .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
            .withEnv(
                "JAVA_TOOL_OPTIONS",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork"
            )
            .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
            .waitingFor(Wait.forHttp("/isReady").forStatusCode(200))
    }

    @Suppress("HttpUrlsUsage") // Kun for lokal kjøring
    fun mappedUrl(): String {
        start()
        return "http://${pdfContainer.host}:${pdfContainer.getMappedPort(8080)}"
    }

    @Synchronized
    private fun start() {
        if (!pdfContainer.isRunning) {
            pdfContainer.start()
        }
    }
}