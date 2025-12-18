package no.nav.pensjon.brev

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import java.util.Objects

@Suppress("unused")
class PDFRequest(
    val letterMarkup: LetterMarkup,
    val attachments: List<LetterMarkup.Attachment>,
    val language: LanguageCode,
    val brevtype: LetterMetadata.Brevtype,
    val pdfVedlegg: List<PDFTittel> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFRequest) return false
        return letterMarkup == other.letterMarkup
                && attachments == other.attachments
                && language == other.language
                && brevtype == other.brevtype
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() = Objects.hash(letterMarkup, language, attachments, language, brevtype, pdfVedlegg)
    override fun toString(): String = "PDFRequest(letterMarkup=$letterMarkup, attachments=$attachments, language=$language, brevtype=$brevtype, pdfVedlegg=$pdfVedlegg)"
}
