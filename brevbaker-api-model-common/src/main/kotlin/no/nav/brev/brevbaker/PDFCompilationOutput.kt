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