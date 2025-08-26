package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.PDFCompilationOutput

sealed class PDFCompilationResponse {
    data class Success(val pdfCompilationOutput: PDFCompilationOutput) : PDFCompilationResponse()

    sealed class Failure: PDFCompilationResponse() {
        abstract val reason: String

        data class Client(override val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(override val reason: String): Failure()
        data class Timeout(override val reason: String): Failure()
        data class QueueTimeout(override val reason: String): Failure()
    }
}
