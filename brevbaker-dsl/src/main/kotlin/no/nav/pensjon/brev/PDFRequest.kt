package no.nav.pensjon.brev

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.util.Objects

@Suppress("unused")
class PDFRequest(
    val letterMarkup: LetterMarkup,
    val attachments: List<LetterMarkup.Attachment>,
    val language: LanguageCode,
    val brevtype: LetterMetadata.Brevtype,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFRequest) return false
        return letterMarkup == other.letterMarkup
                && attachments == other.attachments
                && language == other.language
                && brevtype == other.brevtype
    }

    override fun hashCode() = Objects.hash(letterMarkup, language, attachments, language, brevtype)
    override fun toString(): String {
        return "PDFRequest(letterMarkup=$letterMarkup, attachments=$attachments, language=$language, brevtype=$brevtype)"
    }
}
