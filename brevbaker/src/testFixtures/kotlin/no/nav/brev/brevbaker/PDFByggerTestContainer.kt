package no.nav.brev.brevbaker

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object PDFByggerTestContainer {

    private val pdfContainer: GenericContainer<*> = konfigurerPdfbyggerContainer()

    private fun konfigurerPdfbyggerContainer(): GenericContainer<*> {
        // DIGEST blir i GitHub Actions-byggejobbane sendt inn som miljøvariabel. Lokalt kan vi bruke nyeste bygde.
        // TODO: differensier lokal køyring
        val fullImageName = System.getenv("DIGEST")
            ?.takeIf { it.isNotBlank() }
            ?.let { "ghcr.io/navikt/pensjonsbrev/pdf-bygger:$it" }
            ?: "pensjonsbrev-pdf-bygger:latest"
        return GenericContainer(DockerImageName.parse(fullImageName))
            .withExposedPorts(8080)
            .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
            .withEnv(
                "JAVA_TOOL_OPTIONS",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork"
            )
            .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
            .waitingFor(Wait.forHttp("/isAlive").forStatusCode(200))
    }

    @Suppress("HttpUrlsUsage") // Kun for lokal kjøring
    fun mappedUrl() = "http://${pdfContainer.host}:${pdfContainer.getMappedPort(8080)}"

    @Synchronized
    fun start() {
        if (!pdfContainer.isRunning) {
            pdfContainer.start()
        }
    }
}