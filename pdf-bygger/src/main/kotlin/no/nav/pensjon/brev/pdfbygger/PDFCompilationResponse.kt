package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.PDFCompilationOutput

sealed class PDFCompilationResponse {
    data class Success(val pdfCompilationOutput: PDFCompilationOutput) : PDFCompilationResponse()
    
    sealed class Failure: PDFCompilationResponse() {
        data class Client(val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(val reason: String): Failure()
        data class Timeout(val reason: String): Failure()
        data class QueueTimeout(val reason: String): Failure()
    }
}
