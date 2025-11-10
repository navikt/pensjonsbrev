package no.nav.brev.brevbaker

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object PDFByggerTestContainer {

    val imageDigest = System.getenv("DIGEST") ?: "latest"
    val fullImageName = if (System.getenv("DIGEST")?.isNotEmpty() == true) {
        "ghcr.io/navikt/pensjonsbrev/pdf-bygger:$imageDigest"
    } else {
        "pensjonsbrev-pdf-bygger:latest"
    }

    private val pdfContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse(fullImageName))
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

    @Synchronized
    fun start() {
        if (!pdfContainer.isRunning) {
            println("Starter container for $fullImageName")
            startContainer()
        }
    }

    private fun startContainer() {
        pdfContainer.start()
    }
}