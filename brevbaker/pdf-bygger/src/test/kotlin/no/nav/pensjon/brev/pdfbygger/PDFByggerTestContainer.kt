package no.nav.pensjon.brev.pdfbygger

import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.PullPolicy
import org.testcontainers.utility.DockerImageName
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object PDFByggerTestContainer {
    private val useLocalPdfBygger = System.getenv("BRUK_LOKAL_PDF_BYGGER")?.toBoolean() == true
    private val reuseContainer = System.getenv("TESTCONTAINERS_REUSE_ENABLE")?.toBoolean() == true
    private val pdfContainer: GenericContainer<*> = configurePdfByggerContainer()

    private const val port = 8080

    private fun configurePdfByggerContainer(): GenericContainer<*> {
        val environmentImage = System.getenv("PDF_BYGGER_IMAGE")?.takeIf { it.isNotBlank() }
        val imageName = when {
            environmentImage != null -> environmentImage
            useLocalPdfBygger -> "pensjonsbrev-pdf-bygger:latest"
            else -> "ghcr.io/navikt/pensjonsbrev/pdf-bygger:main"
        }
        val pullPolicy =
            if (environmentImage == null && useLocalPdfBygger) PullPolicy.defaultPolicy() else PullPolicy.alwaysPull()

        return GenericContainer(DockerImageName.parse(imageName))
            .withImagePullPolicy(pullPolicy)
            .withExposedPorts(port)
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("pdf-bygger")))
            .withEnv(
                "JAVA_TOOL_OPTIONS",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5016 -Djdk.lang.Process.launchMechanism=vfork",
            )
            .withEnv("PDF_BYGGER_COMPILE_TMP_DIR", "/tmp")
            .withEnv("PDF_COMPILE_TIMEOUT_SECONDS", "200")
            .waitingFor(
                Wait.forHttp("/isReady")
                    .forStatusCode(200)
                    .withStartupTimeout(50.seconds.toJavaDuration()),
            )
            .withReuse(reuseContainer)
    }

    fun mappedUrl(): String {
        start()
        @Suppress("HttpUrlsUsage")
        return "http://${pdfContainer.host}:${pdfContainer.getMappedPort(port)}"
    }

    @Synchronized
    private fun start() {
        if (!pdfContainer.isRunning) {
            pdfContainer.start()
        }
    }
}
