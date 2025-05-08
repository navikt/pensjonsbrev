package no.nav.brev.brevbaker

@Suppress("unused")
class PDFCompilationOutput(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFCompilationOutput) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode() = bytes.contentHashCode()

    override fun toString() = "PDFCompilationOutput(bytes=... Totalt antall bytes: ${bytes.size})"
}

class AsyncPDFCompilationOutput(
    val base64PDF: String?,
    val error: String?
) {
    override fun toString(): String {
        return "AsyncPDFCompilationOutput(base64PDF=... Size is: ${base64PDF?.length ?: 0}, error = $error"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AsyncPDFCompilationOutput

        if (base64PDF != other.base64PDF) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = base64PDF?.hashCode() ?: 0
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}