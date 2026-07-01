package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent

interface AttachmentTitle {
    val title: List<ParagraphContent.Text>
}

interface AttachmentTitleV2 {
    val title1: List<LetterMarkupV2.Text>
}

class PDFTittel(override val title: List<ParagraphContent.Text>) : AttachmentTitle {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTittel) return false
        return title == other.title
    }
    override fun hashCode() = title.hashCode()
    override fun toString() = "PDFTittel(title=$title)"
}

class PDFTittelV2(override val title1: List<LetterMarkupV2.Text>) : AttachmentTitleV2 {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTittelV2) return false
        return title1 == other.title1
    }
    override fun hashCode() = title1.hashCode()
    override fun toString() = "PDFTittelV2(title1=$title1)"
}