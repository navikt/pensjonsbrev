package no.nav.pensjon.brev.pdfbygger.latex

import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.template.render.DocumentFile
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class BlockingLatexService(
    val latexParallelism: Int,
    private val queueWaitTimeout: Duration,
    private val latexCompileService: LatexCompileService
) {
    constructor(config: ApplicationConfig) : this(
        latexParallelism = config.propertyOrNull("latexParallelism")?.getString()?.toInt()
            ?: Runtime.getRuntime().availableProcessors(),
        queueWaitTimeout = config.propertyOrNull("compileQueueWaitTimeout")?.getString()
            ?.let { Duration.parse(it) }
            ?: 4.seconds,
        latexCompileService = LatexCompileService(config)
    )

    private val logger = LoggerFactory.getLogger(BlockingLatexService::class.java)
    private val parallelismSemaphore = latexParallelism.takeIf { it > 0 }?.let { Semaphore(it) }

    suspend fun producePDF(latexFiles: List<DocumentFile>): PDFCompilationResponse {
        return if (parallelismSemaphore != null) {
            val permit = withTimeoutOrNull(queueWaitTimeout) {
                parallelismSemaphore.acquire()
            }
            if (permit != null) {
                try {
                    latexCompileService.createLetter(latexFiles)
                } finally {
                    parallelismSemaphore.release()
                }
            } else {
                logger.info("Compilation queue wait timed out after $queueWaitTimeout")
                PDFCompilationResponse.Failure.QueueTimeout(reason = "Compilation queue wait timed out: waited for $queueWaitTimeout")
            }
        } else {
            latexCompileService.createLetter(latexFiles)
        }
    }
}