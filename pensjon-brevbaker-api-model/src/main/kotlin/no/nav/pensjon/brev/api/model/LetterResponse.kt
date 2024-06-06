package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata) {
    data class V2(val file: ByteArray, val contentType: String, val letterMetadata: LetterMetadata) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as V2

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
}