package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.ParagraphContentV2

interface AttachmentTitle {
    val title: List<ParagraphContent.Text>
}

interface AttachmentTitleV2 {
    val title: List<ParagraphContentV2.Text>
}

class PDFTittel(override val title: List<ParagraphContent.Text>) : AttachmentTitle {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTittel) return false
        return title == other.title
    }
    override fun hashCode() = title.hashCode()
    override fun toString() = "PDFTittel(title=$title)"
}