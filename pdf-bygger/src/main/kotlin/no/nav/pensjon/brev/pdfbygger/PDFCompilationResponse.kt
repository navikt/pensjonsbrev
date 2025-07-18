package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.PDFCompilationOutput

sealed class PDFCompilationResponse {
    data class Success(val pdfCompilationOutput: PDFCompilationOutput) : PDFCompilationResponse(){
        override fun hashCode(): Int = pdfCompilationOutput.hashCode()
        override fun equals(other: Any?): Boolean {
            if (other !is Success) return false
            return pdfCompilationOutput != other.pdfCompilationOutput
        }
        override fun toString(): String = "Success(pdfCompilationOutput=$pdfCompilationOutput)"

    }
    
    sealed class Failure: PDFCompilationResponse() {
        abstract val reason: String

        data class Client(override val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(override val reason: String): Failure()
        data class Timeout(override val reason: String): Failure()
        data class QueueTimeout(override val reason: String): Failure()
    }
}
