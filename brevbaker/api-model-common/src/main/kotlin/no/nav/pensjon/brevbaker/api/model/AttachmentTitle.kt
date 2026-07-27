package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent

interface AttachmentTitle {
    val title: List<ParagraphContent.Text>
}

class PDFTittel(override val title: List<ParagraphContent.Text>) : AttachmentTitle {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTittel) return false
        return title == other.title
    }
    override fun hashCode() = title.hashCode()
    override fun toString() = "PDFTittel(title=$title)"
}

class PDFVedleggTittel(val titler: Map<LanguageCode, String>) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedleggTittel) return false
        return titler == other.titler
    }
    override fun hashCode() = titler.hashCode()
    override fun toString() = "PDFVedleggTittel(titler=$titler)"
}