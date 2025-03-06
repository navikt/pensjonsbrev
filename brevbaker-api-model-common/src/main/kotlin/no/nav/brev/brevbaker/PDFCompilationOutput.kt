package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser

interface PDFCompilationOutput {
    val bytes: ByteArray
}

@InterneDataklasser
data class PDFCompilationOutputImpl(override val bytes: ByteArray) : PDFCompilationOutput {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PDFCompilationOutputImpl

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}