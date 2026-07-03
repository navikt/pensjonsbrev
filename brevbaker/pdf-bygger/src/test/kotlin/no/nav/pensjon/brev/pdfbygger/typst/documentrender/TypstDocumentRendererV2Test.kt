package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequestV2
import no.nav.pensjon.brev.pdfbygger.attachmentV2
import no.nav.pensjon.brev.pdfbygger.letterMarkupV2
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittelV2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

/**
 * Unit tests for [TypstDocumentRendererV2] that inspect the generated Typst source
 * directly (string-level assertions), without requiring the `typst` binary or a
 * dockerized compile service. Mirrors the intent of [TypstDocumentRenderer]'s
 * happy-path coverage in [no.nav.pensjon.brev.pdfbygger.PdfByggerAppTest], but
 * targets the renderer in isolation.
 */
class TypstDocumentRendererV2Test {

    private fun render(request: PDFRequestV2): String {
        val output = ByteArrayOutputStream()
        OutputStreamWriter(output, Charsets.UTF_8).use { writer ->
            TypstDocumentRendererV2.render(request, TypstFileWriter(writer))
        }
        return output.toString(Charsets.UTF_8)
    }

    @Test
    fun `renderer skriver ut brevtittel og importer riktige typst-funksjoner`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("En fin tittel") }
                outline {
                    paragraph { text("Hei, dette er et brev.") }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("""#import "template.typ": template""")
        assertThat(typst).contains("""#import "content/title.typ": title1, title2, title3""")
        assertThat(typst).contains("""lettertitle: "En fin tittel",""")
        assertThat(typst).contains("Hei, dette er et brev.")
        assertThat(typst).contains("closing(input, languageSettings)")
        assertThat(typst).contains("""<endOfLetter>""")
    }

    @Test
    fun `title2, title3 og title4 blokker mapper til title1, title2 og title3 typst-funksjoner`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Brevtittel") }
                outline {
                    title2 { text("Overskrift nivå 2") }
                    title3 { text("Overskrift nivå 3") }
                    title4 { text("Overskrift nivå 4") }
                    paragraph { text("Innhold") }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("title1[").contains("Overskrift nivå 2")
        assertThat(typst).contains("title2[").contains("Overskrift nivå 3")
        assertThat(typst).contains("title3[").contains("Overskrift nivå 4")
    }

    @Test
    fun `itemlist, numberedlist og table renderes som egne blokker`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                outline {
                    paragraph { text("Før liste") }
                    list {
                        item { text("Punkt 1") }
                        item { text("Punkt 2") }
                    }
                    numberedList {
                        item { text("Nummer 1") }
                    }
                    table(
                        header = { column { text("Kolonne 1") } },
                    ) {
                        row { cell { text("Celle 1") } }
                    }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("bulletlist")
        assertThat(typst).contains("numberedlist")
        assertThat(typst).contains("letter-table")
        assertThat(typst).contains("Punkt 1")
        assertThat(typst).contains("Nummer 1")
        assertThat(typst).contains("Celle 1")
    }

    @Test
    fun `vedlegg renderes med startAttachment og endAttachment`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                outline { paragraph { text("Innhold") } }
            },
            attachments = listOf(
                attachmentV2 {
                    title1 { text("Vedleggstittel") }
                    outline { paragraph { text("Vedleggsinnhold") } }
                }
            ),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("startAttachment")
        assertThat(typst).contains("endAttachment")
        assertThat(typst).contains("Vedleggstittel")
        assertThat(typst).contains("Vedleggsinnhold")
    }

    @Test
    fun `saksbehandlersignatur mappes inn i input-dictionary`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                signatur {
                    saksbehandlerNavn = "Kari Saksbehandler"
                    attesterendeSaksbehandlerNavn = "Ola Attestant"
                }
                outline { paragraph { text("Innhold") } }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("Kari Saksbehandler")
        assertThat(typst).contains("Ola Attestant")
    }

    @Test
    fun `manglende saksbehandlersignatur gir none i stedet for feil`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                signatur {
                    saksbehandlerNavn = null
                }
                outline { paragraph { text("Innhold") } }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("signerendeSaksbehandler: none")
    }

    @Test
    fun `formChoice renderes med formChoice-funksjonen fra content-form typ`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                outline {
                    paragraph { text("Før skjema") }
                    formChoice {
                        prompt { text("Velg et alternativ") }
                        choice { text("Ja") }
                        choice { text("Nei") }
                    }
                    paragraph { text("Etter skjema") }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("""#import "content/form.typ": formChoice, formText""")
        assertThat(typst).contains("formChoice[")
        assertThat(typst).contains("Velg et alternativ")
        assertThat(typst).contains("Ja")
        assertThat(typst).contains("Nei")
    }

    @Test
    fun `formText renderes med formText-funksjonen fra content-form typ`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                outline {
                    paragraph { text("Før skjema") }
                    formText(size = Block.FormText.Size.SHORT) {
                        prompt { text("Skriv inn navn") }
                    }
                    paragraph { text("Etter skjema") }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        val typst = render(request)

        assertThat(typst).contains("""#import "content/form.typ": formChoice, formText""")
        assertThat(typst).contains("formText[")
        assertThat(typst).contains("Skriv inn navn")
    }

    @OptIn(InterneDataklasser::class)
    @Test
    fun `pdfVedlegg med PDFTittelV2 renderes i attachments-listen`() {
        val request = PDFRequestV2(
            letterMarkup = letterMarkupV2 {
                title1 { text("Tittel") }
                outline { paragraph { text("Innhold") } }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            pdfVedlegg = listOf(PDFTittelV2(listOf(LiteralImpl(1, "Skannet vedleggstittel")))),
        )

        val typst = render(request)

        assertThat(typst).contains("Skannet vedleggstittel")
    }
}
