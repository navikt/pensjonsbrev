package no.nav.pensjon.brev

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittelV2
import java.util.Objects

@Suppress("unused")
class PDFRequestV2(
    val letterMarkup: LetterMarkupV2,
    val attachments: List<LetterMarkupV2.Attachment>,
    val language: LanguageCode,
    val brevtype: LetterMetadata.Brevtype,
    val pdfVedlegg: List<PDFTittelV2> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFRequestV2) return false
        return letterMarkup == other.letterMarkup
                && attachments == other.attachments
                && language == other.language
                && brevtype == other.brevtype
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() = Objects.hash(letterMarkup, language, attachments, language, brevtype, pdfVedlegg)
    override fun toString(): String {
        return "PDFRequestV2(letterMarkup=$letterMarkup, attachments=$attachments, language=$language, brevtype=$brevtype, pdfVedlegg=$pdfVedlegg)"
    }
}
