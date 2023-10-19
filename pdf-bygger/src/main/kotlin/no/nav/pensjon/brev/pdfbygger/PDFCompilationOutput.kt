package no.nav.pensjon.brev.pdfbygger

sealed class PDFCompilationResponse {
    data class Base64PDF(val base64PDF: String): PDFCompilationResponse()
    sealed class Failure: PDFCompilationResponse() {
        data class Client(val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(val reason: String): Failure()
        data class Timeout(val reason: String): Failure()
        data class ServiceUnavailable(val reason: String): Failure()
    }
}
