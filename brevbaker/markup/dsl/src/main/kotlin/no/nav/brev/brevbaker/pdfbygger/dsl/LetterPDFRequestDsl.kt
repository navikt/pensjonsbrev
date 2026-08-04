package no.nav.brev.brevbaker.pdfbygger.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.dsl.AttachmentBuilder
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.MarkupDsl
import no.nav.brev.brevbaker.markup.dsl.attachment as buildAttachment
import no.nav.brev.brevbaker.markup.dsl.pdfTittel
import no.nav.brev.brevbaker.pdfbygger.api.letterPDFRequestModel

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
        attachments.add(buildAttachment(inkluderSaksinformasjon, build))
    }

    /** Legg til en ferdig bygget PDF-vedlegg-tittel. */
    fun pdfVedlegg(tittel: PDFTittel) {
        pdfVedlegg.add(tittel)
    }

    /** Bygg og legg til en PDF-vedlegg-tittel via [pdfTittel]-DSL-en. */
    fun pdfVedlegg(content: ContentBuilder.() -> Unit) {
        pdfVedlegg.add(pdfTittel(content))
    }

    internal fun build(): LetterPDFRequest = letterPDFRequestModel(
        letterMarkup = letter,
        attachments = attachments.toList(),
        pdfVedlegg = pdfVedlegg.toList(),
        spraak = spraak,
        brevtype = brevtype,
    )
}
