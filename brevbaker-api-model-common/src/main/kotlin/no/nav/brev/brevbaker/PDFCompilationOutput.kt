package no.nav.brev.brevbaker

@Suppress("unused")
class PDFCompilationOutput(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFCompilationOutput) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    override fun toString(): String {
        return "PDFCompilationOutput(bytes=... Totalt antall bytes: ${bytes.size})"
    }
}