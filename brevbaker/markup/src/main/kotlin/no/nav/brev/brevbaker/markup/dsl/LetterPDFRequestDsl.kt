package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.PDFTittel

/**
 * Bygg en [LetterPDFRequest] via DSL. Hovedbrevet [letter] er obligatorisk og angis som argument.
 * Legg eventuelt til brevvedlegg ([PDFRequestBuilder.attachment]) og PDF-vedlegg-titler
 * ([PDFRequestBuilder.pdfVedlegg]) i DSL-blokken.
 *
 * ```
 * val request = letterPDFRequest(
 *     spraak = Markup.Spraak.BOKMAL,
 *     brevtype = Markup.Brevtype.VEDTAKSBREV,
 *     letter = letterMarkup(...) { ... },
 * ) {
 *     attachment(attachment { ... })
 *     pdfVedlegg(pdfTittel { ... })
 * }
 * ```
 */
fun letterPDFRequest(
    spraak: Markup.Spraak,
    brevtype: Markup.Brevtype,
    letter: LetterMarkup,
    build: PDFRequestBuilder.() -> Unit = {},
): LetterPDFRequest = PDFRequestBuilder(spraak, brevtype, letter).apply(build).build()

@MarkupDsl
class PDFRequestBuilder internal constructor(
    private val spraak: Markup.Spraak,
    private val brevtype: Markup.Brevtype,
    private val letter: LetterMarkup,
) {
    private val attachments = mutableListOf<Attachment>()
    private val pdfVedlegg = mutableListOf<PDFTittel>()

    /** Legg til et ferdig bygget brevvedlegg. */
    fun attachment(attachment: Attachment) {
        attachments.add(attachment)
    }

    /** Bygg og legg til et brevvedlegg via [attachment]-DSL-en. */
    fun attachment(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ContentBuilder>.() -> Unit) {
        attachments.add(AttachmentBuilder(::ContentBuilder, inkluderSaksinformasjon).apply(build).build())
    }

    /** Legg til en ferdig bygget PDF-vedlegg-tittel. */
    fun pdfVedlegg(tittel: PDFTittel) {
        pdfVedlegg.add(tittel)
    }

    /** Bygg og legg til en PDF-vedlegg-tittel via [pdfTittel]-DSL-en. */
    fun pdfVedlegg(content: ContentBuilder.() -> Unit) {
        pdfVedlegg.add(PDFTittel(ContentBuilder().apply(content).build()))
    }

    internal fun build(): LetterPDFRequest = LetterPDFRequest(
        letterMarkup = letter,
        attachments = attachments.toList(),
        pdfVedlegg = pdfVedlegg.toList(),
        spraak = spraak,
        brevtype = brevtype,
    )
}
