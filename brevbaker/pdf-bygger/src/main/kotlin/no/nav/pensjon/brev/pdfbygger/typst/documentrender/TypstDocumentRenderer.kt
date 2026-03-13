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
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

private const val DOCUMENT_PRODUCER = "brevbaker / pdf-bygger med typst"

object TypstDocumentRenderer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    internal fun render(pdfRequest: PDFRequest): TypstDocument = TypstDocumentRenderer.render(
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
            newTypstFile("input.typ") {
                appendDictionary("settings", documentLanguageSettings.languageSettings(language))
                appendDictionary("input",
                    mapOf(
                        "gjelderNavn" to letter.sakspart.gjelderNavn,
                        "gjelderFoedselsnummer" to letter.sakspart.gjelderFoedselsnummer.value,
                        "annenMottakerNavn" to letter.sakspart.annenMottakerNavn,
                        "saksnummer" to letter.sakspart.saksnummer,
                        "dokumentDato" to letter.sakspart.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    )
                )
            }

            // todo params
            // todo xmpdata
            newTypstFile("letter.typ") { renderLetterTemplate(letter, attachments) }
            // todo attachments
        }.also {
            it.files.forEach { file ->
                println(file.fileName + ":" + file.content)
            }
        }
    }

    private fun TypstAppendable.renderLetterTemplate(letter: LetterMarkup, attachments: List<LetterMarkup.Attachment>) {

    }
}