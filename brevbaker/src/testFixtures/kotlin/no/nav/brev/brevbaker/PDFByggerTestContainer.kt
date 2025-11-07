package no.nav.brev.brevbaker

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object PDFByggerTestContainer {

    private val pdfContainer: GenericContainer<*> = GenericContainer(
        DockerImageName.parse(
            // TODO: Skal erstatte denne med main, må berre fikse eigen byggejobb først
            "ghcr.io/navikt/pensjonsbrev/pdf-bygger:f9c10987e741be0ceb5340f9a788b3b70b11976a"
        )
    )
        .withExposedPorts(8080)
        .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
        .withEnv(
            "JAVA_TOOL_OPTIONS",
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork"
        )
        .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
        .waitingFor(Wait.forHttp("/isAlive").forStatusCode(200))

    @Suppress("HttpUrlsUsage") // Kun for lokal kjøring
    fun mappedUrl() = "http://${pdfContainer.host}:${pdfContainer.getMappedPort(8080)}"

    fun start() {
        if (!pdfContainer.isRunning) {
            pdfContainer.start()
        }
    }

    fun stop() = pdfContainer.stop()
}