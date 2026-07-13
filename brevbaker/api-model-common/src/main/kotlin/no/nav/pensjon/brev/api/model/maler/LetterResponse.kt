package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

@Suppress("unused")
// TODO: fjern default-verdi for pdfvedlegg når både skribenten og brevbaker er deploya
class LetterResponse(val file: ByteArray, val contentType: String, val letterMetadata: LetterMetadata, val pdfvedlegg: List<PDFVedlegg> = emptyList()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LetterResponse

        if (!file.contentEquals(other.file)) return false
        if (contentType != other.contentType) return false
        if (letterMetadata != other.letterMetadata) return false
        if (pdfvedlegg != other.pdfvedlegg) return false

        return true
    }

    override fun hashCode(): Int {
        var result = file.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + letterMetadata.hashCode()
        result = 31 * result + pdfvedlegg.hashCode()
        return result
    }

    override fun toString() = "LetterResponse(file=${file.contentToString()}, contentType='$contentType', letterMetadata=$letterMetadata, pdfVedlegg=$pdfvedlegg)"
}