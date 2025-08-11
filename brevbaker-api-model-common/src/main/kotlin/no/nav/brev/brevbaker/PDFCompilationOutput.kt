package no.nav.brev.brevbaker

import java.util.Objects

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
        if (other !is AsyncPDFCompilationOutput) return false
        return base64PDF == other.base64PDF
                && error == other.error
    }

    override fun hashCode(): Int = Objects.hash(base64PDF.hashCode(), error.hashCode())
}