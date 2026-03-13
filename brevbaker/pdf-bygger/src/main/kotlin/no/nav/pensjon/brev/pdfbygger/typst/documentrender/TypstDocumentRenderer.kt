package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.pdfbygger.clean
import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocument
import no.nav.pensjon.brev.pdfbygger.latex.documentRender.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brev.pdfbygger.typst.TypstDocument
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import org.slf4j.LoggerFactory

object TypstDocumentRenderer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    internal fun render(pdfRequest: PDFRequest): LatexDocument = LatexDocumentRenderer.render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments.clean(),
        language = pdfRequest.language.toLanguage(),
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
    )

    private fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittel>,
    ): TypstDocument {
        return TypstDocument().apply {
            // todo params
            // todo xmpdata
            newTypstFile("letter.typ") { renderLetterTemplate(letter, attachments) }
            // todo attachments
        }
    }

    private fun TypstAppendable.renderLetterTemplate(letter: LetterMarkup, attachments: List<LetterMarkup.Attachment>) {

    }
}