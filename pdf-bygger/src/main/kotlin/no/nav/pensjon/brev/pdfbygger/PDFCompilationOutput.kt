package no.nav.pensjon.brev.pdfbygger

internal sealed class PDFCompilationResponse {
    data class Bytes(val bytes: ByteArray): PDFCompilationResponse() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Bytes

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    sealed class Failure: PDFCompilationResponse() {
        data class Client(val reason: String, val output: String? = null, val error: String? = null): Failure()
        data class Server(val reason: String): Failure()
        data class Timeout(val reason: String): Failure()
        data class QueueTimeout(val reason: String): Failure()
    }
}
