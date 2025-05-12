package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.PDFCompilationOutput

sealed class PDFCompilationResponse {
    class Success(val pdfCompilationOutput: PDFCompilationOutput) : PDFCompilationResponse(){
        override fun hashCode(): Int = pdfCompilationOutput.hashCode()
        override fun equals(other: Any?): Boolean {
            if (other !is Success) return false
            return pdfCompilationOutput != other.pdfCompilationOutput
        }
        override fun toString(): String = "Success(pdfCompilationOutput=$pdfCompilationOutput)"

    }
    
    sealed class Failure: PDFCompilationResponse() {
        class Client(val reason: String, val output: String? = null, val error: String? = null): Failure(){

        }
        class Server(val reason: String): Failure(){

        }
        class Timeout(val reason: String): Failure(){

        }
        class QueueTimeout(val reason: String): Failure(){

        }
    }
}
