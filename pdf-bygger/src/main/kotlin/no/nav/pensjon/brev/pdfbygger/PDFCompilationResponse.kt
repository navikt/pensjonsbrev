package no.nav.pensjon.brev.pdfbygger

internal sealed class PDFCompilationResponse {
    data class Base64PDF(val base64PDF: String) : PDFCompilationResponse()

    sealed class Failure : PDFCompilationResponse() {
        data class Client(val reason: String, val output: String? = null, val error: String? = null) : Failure()

        data class Server(val reason: String) : Failure()

        data class Timeout(val reason: String) : Failure()

        data class QueueTimeout(val reason: String) : Failure()
    }
}
