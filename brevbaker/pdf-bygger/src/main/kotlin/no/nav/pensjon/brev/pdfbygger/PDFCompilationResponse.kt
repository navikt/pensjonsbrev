package no.nav.pensjon.brev.pdfbygger

sealed class PDFCompilationResponse {
    data object Success : PDFCompilationResponse()

    sealed class Failure: PDFCompilationResponse() {
        abstract val reason: String

        data class Client(override val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(override val reason: String): Failure()
    }
}
