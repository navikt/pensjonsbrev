package no.nav.brev.brevbaker.markup


/**
 * En ferdig bestilling som pdf-bygger kan rendre til PDF: hovedbrevet, eventuelle brevvedlegg og
 * frittstående PDF-vedlegg-titler, sammen med språk og brevtype.
 */
data class LetterPDFRequest @MarkupInternalApi constructor(
    val letterMarkup: LetterMarkup,
    val attachments: List<Attachment>,
    val pdfVedlegg: List<PDFTittel>,
    val spraak: Markup.Spraak,
    val brevtype: Markup.Brevtype,
)
