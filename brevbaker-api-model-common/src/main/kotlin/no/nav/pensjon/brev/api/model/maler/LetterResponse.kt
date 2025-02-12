package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class LetterResponse(val file: ByteArray, val contentType: String, val letterMetadata: LetterMetadata) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LetterResponse

        if (!file.contentEquals(other.file)) return false
        if (contentType != other.contentType) return false
        if (letterMetadata != other.letterMetadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = file.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + letterMetadata.hashCode()
        return result
    }
}
