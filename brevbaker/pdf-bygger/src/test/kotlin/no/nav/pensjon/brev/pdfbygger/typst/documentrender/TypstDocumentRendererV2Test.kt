package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.LetterMarkupBuilder
import no.nav.brev.brevbaker.markup.dsl.cell
import no.nav.brev.brevbaker.markup.dsl.choice
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.formChoice
import no.nav.brev.brevbaker.markup.dsl.formText
import no.nav.brev.brevbaker.markup.dsl.header
import no.nav.brev.brevbaker.markup.dsl.item
import no.nav.brev.brevbaker.markup.dsl.itemList
import no.nav.brev.brevbaker.markup.dsl.letterMarkup
import no.nav.brev.brevbaker.markup.dsl.letterPDFRequest
import no.nav.brev.brevbaker.markup.dsl.numberedList
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.prompt
import no.nav.brev.brevbaker.markup.dsl.row
import no.nav.brev.brevbaker.markup.dsl.saksinformasjon
import no.nav.brev.brevbaker.markup.dsl.signatur
import no.nav.brev.brevbaker.markup.dsl.table
import no.nav.brev.brevbaker.markup.dsl.title1
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.dsl.title3
import no.nav.brev.brevbaker.markup.dsl.title4
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate

/**
 * Unit tests for [TypstDocumentRendererV2] that inspect the generated Typst source
 * directly (string-level assertions), without requiring the `typst` binary or a
 * dockerized compile service. Bygger markup via markup-modulens DSL.
 */
class TypstDocumentRendererV2Test {

    private fun render(request: LetterPDFRequest): String {
        val output = ByteArrayOutputStream()
        OutputStreamWriter(output, Charsets.UTF_8).use { writer ->
            TypstDocumentRendererV2.render(request, TypstFileWriter(writer))
        }
        return output.toString(Charsets.UTF_8)
    }

    private val saksinformasjon = saksinformasjon(
        gjelderNavn = "Navn Navnesen",
        gjelderPersonidentifikator = "12345678901",
        saksnummer = "123",
        dokumentDato = LocalDate.of(2025, 1, 1),
    )

    private fun brev(
        signatur: Signatur = signatur("hilsen", "Nav sentralt", saksbehandlerNavn = "Saksbehandler Saksbehandlersen"),
        build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
    ): LetterMarkup = letterMarkup(saksinformasjon = saksinformasjon, signatur = signatur, build = build)

    private fun request(letter: LetterMarkup): LetterPDFRequest =
        letterPDFRequest(spraak = Markup.Spraak.BOKMAL, brevtype = Markup.Brevtype.VEDTAKSBREV, letter = letter)

    @Test
    fun `renderer skriver ut brevtittel og importer riktige typst-funksjoner`() {
        val typst = render(
            request(
                brev {
                    title1("En fin tittel")
                    outline {
                        paragraph("Hei, dette er et brev.")
                    }
                }
            )
        )

        assertThat(typst).contains("""#import "template.typ": template""")
        assertThat(typst).contains("""#import "content/title.typ": title1, title2, title3""")
        assertThat(typst).contains("""lettertitle: "En fin tittel",""")
        assertThat(typst).contains("Hei, dette er et brev.")
        assertThat(typst).contains("closing(input, languageSettings)")
        assertThat(typst).contains("""<endOfLetter>""")
    }

    @Test
    fun `title2, title3 og title4 blokker mapper til title1, title2 og title3 typst-funksjoner`() {
        val typst = render(
            request(
                brev {
                    title1("Brevtittel")
                    outline {
                        title2("Overskrift nivå 2")
                        title3("Overskrift nivå 3")
                        title4("Overskrift nivå 4")
                        paragraph("Innhold")
                    }
                }
            )
        )

        assertThat(typst).contains("title1[").contains("Overskrift nivå 2")
        assertThat(typst).contains("title2[").contains("Overskrift nivå 3")
        assertThat(typst).contains("title3[").contains("Overskrift nivå 4")
    }

    @Test
    fun `itemlist, numberedlist og table renderes som egne blokker`() {
        val typst = render(
            request(
                brev {
                    title1("Tittel")
                    outline {
                        paragraph("Før liste")
                        itemList {
                            item("Punkt 1")
                            item("Punkt 2")
                        }
                        numberedList {
                            item("Nummer 1")
                        }
                        table {
                            header { column("Kolonne 1") }
                            row { cell("Celle 1") }
                        }
                    }
                }
            )
        )

        assertThat(typst).contains("bulletlist")
        assertThat(typst).contains("numberedlist")
        assertThat(typst).contains("letter-table")
        assertThat(typst).contains("Punkt 1")
        assertThat(typst).contains("Nummer 1")
        assertThat(typst).contains("Celle 1")
    }

    @Test
    fun `vedlegg renderes med startAttachment og endAttachment`() {
        val request = letterPDFRequest(
            spraak = Markup.Spraak.BOKMAL,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            letter = brev {
                title1("Tittel")
                outline { paragraph("Innhold") }
            },
        ) {
            attachment {
                title1("Vedleggstittel")
                outline { paragraph("Vedleggsinnhold") }
            }
        }

        val typst = render(request)

        assertThat(typst).contains("startAttachment")
        assertThat(typst).contains("endAttachment")
        assertThat(typst).contains("Vedleggstittel")
        assertThat(typst).contains("Vedleggsinnhold")
    }

    @Test
    fun `saksbehandlersignatur mappes inn i input-dictionary`() {
        val typst = render(
            request(
                brev(
                    signatur = signatur(
                        "hilsen",
                        "Nav sentralt",
                        saksbehandlerNavn = "Kari Saksbehandler",
                        attesterendeSaksbehandlerNavn = "Ola Attestant",
                    )
                ) {
                    title1("Tittel")
                    outline { paragraph("Innhold") }
                }
            )
        )

        assertThat(typst).contains("Kari Saksbehandler")
        assertThat(typst).contains("Ola Attestant")
    }

    @Test
    fun `manglende saksbehandlersignatur gir none i stedet for feil`() {
        val typst = render(
            request(
                brev(signatur = signatur("hilsen", "Nav sentralt")) {
                    title1("Tittel")
                    outline { paragraph("Innhold") }
                }
            )
        )

        assertThat(typst).contains("signerendeSaksbehandler: none")
    }

    @Test
    fun `formChoice renderes med formChoice-funksjonen fra content-form typ`() {
        val typst = render(
            request(
                brev {
                    title1("Tittel")
                    outline {
                        paragraph("Før skjema")
                        formChoice {
                            prompt("Velg et alternativ")
                            choice("Ja")
                            choice("Nei")
                        }
                        paragraph("Etter skjema")
                    }
                }
            )
        )

        assertThat(typst).contains("""#import "content/form.typ": formChoice, formText""")
        assertThat(typst).contains("formChoice[")
        assertThat(typst).contains("Velg et alternativ")
        assertThat(typst).contains("Ja")
        assertThat(typst).contains("Nei")
    }

    @Test
    fun `formText renderes med formText-funksjonen fra content-form typ`() {
        val typst = render(
            request(
                brev {
                    title1("Tittel")
                    outline {
                        paragraph("Før skjema")
                        formText(Size.SHORT) { text("Skriv inn navn") }
                        paragraph("Etter skjema")
                    }
                }
            )
        )

        assertThat(typst).contains("""#import "content/form.typ": formChoice, formText""")
        assertThat(typst).contains("formText[")
        assertThat(typst).contains("Skriv inn navn")
    }

    @Test
    fun `pdfVedlegg med PDFTittel renderes i attachments-listen`() {
        val request = letterPDFRequest(
            spraak = Markup.Spraak.BOKMAL,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            letter = brev {
                title1("Tittel")
                outline { paragraph("Innhold") }
            },
        ) {
            pdfVedlegg { text("Skannet vedleggstittel") }
        }

        val typst = render(request)

        assertThat(typst).contains("Skannet vedleggstittel")
    }
}
