package no.nav.pensjon.brev.pdfbygger.latex

import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.template.render.DocumentFile
import kotlin.time.Duration

class BlockingLatexService(
    latexParallelism: Int,
    private val queueWaitTimeout: Duration,
    private val latexCompileService: LatexCompileService
) {
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
                PDFCompilationResponse.Failure.QueueTimeout(reason = "Compilation queue wait timed out: waited for $queueWaitTimeout")
            }
        } else {
            latexCompileService.createLetter(latexFiles)
        }
    }
}