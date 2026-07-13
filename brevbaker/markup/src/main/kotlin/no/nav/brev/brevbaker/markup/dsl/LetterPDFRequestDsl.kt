package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.Brevtype
import no.nav.brev.brevbaker.markup.LanguageCode
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.PDFTittel

/**
 * Bygg en [LetterPDFRequest] via DSL. Angi hovedbrevet med [PDFRequestBuilder.letter], og legg
 * eventuelt til brevvedlegg ([PDFRequestBuilder.attachment]) og PDF-vedlegg-titler
 * ([PDFRequestBuilder.pdfVedlegg]).
 *
 * ```
 * val request = letterPDFRequest(language = LanguageCode.BOKMAL, brevtype = Brevtype.VEDTAKSBREV) {
 *     letter(letterMarkup { ... })
 *     attachment(attachment { ... })
 *     pdfVedlegg(pdfTittel { ... })
 * }
 * ```
 */
fun letterPDFRequest(
    language: LanguageCode,
    brevtype: Brevtype,
    build: PDFRequestBuilder.() -> Unit,
): LetterPDFRequest = PDFRequestBuilder(language, brevtype).apply(build).build()

@MarkupDsl
class PDFRequestBuilder internal constructor(
    private val language: LanguageCode,
    private val brevtype: Brevtype,
) {
    private var letter: LetterMarkup? = null
    private val attachments = mutableListOf<Attachment>()
    private val pdfVedlegg = mutableListOf<PDFTittel>()

    /** Sett hovedbrevet fra en ferdig bygget [LetterMarkup]. */
    fun letter(letter: LetterMarkup) {
        this.letter = letter
    }

    /** Bygg og sett hovedbrevet via [letterMarkup]-DSL-en. */
    fun letter(build: LetterMarkupBuilder<ContentBuilder>.() -> Unit) {
        this.letter = letterMarkup(build)
    }

    /** Legg til et ferdig bygget brevvedlegg. */
    fun attachment(attachment: Attachment) {
        attachments.add(attachment)
    }

    /** Bygg og legg til et brevvedlegg via [attachment]-DSL-en. */
    fun attachment(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ContentBuilder>.() -> Unit) {
        attachments.add(AttachmentBuilder(IdGenerator(), ::ContentBuilder, inkluderSaksinformasjon).apply(build).build())
    }

    /** Legg til en ferdig bygget PDF-vedlegg-tittel. */
    fun pdfVedlegg(tittel: PDFTittel) {
        pdfVedlegg.add(tittel)
    }

    /** Bygg og legg til en PDF-vedlegg-tittel via [pdfTittel]-DSL-en. */
    fun pdfVedlegg(content: ContentBuilder.() -> Unit) {
        pdfVedlegg.add(PDFTittel(IdGenerator().content(::ContentBuilder, content)))
    }

    internal fun build(): LetterPDFRequest = LetterPDFRequest(
        letterMarkup = requireNotNull(letter) { "LetterPDFRequest must have a letter" },
        attachments = attachments.toList(),
        pdfVedlegg = pdfVedlegg.toList(),
        language = language,
        brevtype = brevtype,
    )
}
