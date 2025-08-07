package no.nav.pensjon.brev

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.util.Objects
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg

@Suppress("unused")
class PDFRequest(
    val letterMarkup: LetterMarkup,
    val attachments: List<LetterMarkup.Attachment>,
    val language: LanguageCode,
    val brevtype: LetterMetadata.Brevtype,
    val pdfVedlegg: List<PDFVedlegg> = emptyList()
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
    override fun toString(): String {
        return "PDFRequest(letterMarkup=$letterMarkup, attachments=$attachments, language=$language, brevtype=$brevtype, pdfVedlegg=$pdfVedlegg)"
    }
}

class PDFRequestAsync(
    val request: PDFRequest,
    val messageId: String,
    val replyTopic: String,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFRequestAsync) return false
        return request == other.request
                && messageId == other.messageId
                && replyTopic == other.replyTopic
    }

    override fun hashCode() = Objects.hash(request, messageId, replyTopic)

    override fun toString(): String = "PDFRequestAsync(request=$request, messageId='$messageId', replyTopic='$replyTopic')"

}