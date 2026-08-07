package no.nav.brev.brevbaker.serialization

import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.dsl.extended.*
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.pdfbygger.api.letterPDFRequest
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import java.time.LocalDate

/**
 * Én markup som dekker *alle* element-typene. Brukes av golden-JSON-testene til å låse wire-formatet
 * mot pdf-bygger og skribenten, slik at det ikke kan endre seg ubemerket.
 *
 * Alle id-er er deterministiske og datoene faste; fixturen må aldri endres uten at golden-filene
 * oppdateres bevisst (kjør testene med `REGENERER_GOLDEN=true`).
 */
object MarkupGoldenFixture {

    fun letter(): LetterMarkup {
        var next = 0
        fun id() = next++
        return letterMarkupExtended(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
                annenMottakerNavn = "Kari Nordmann",
            ),
            signatur = signatur(
                navAvsenderEnhet = "Nav Familie- og pensjonsytelser",
                saksbehandlerNavn = "Sak S. Behandler",
                attesterendeSaksbehandlerNavn = "Att Esterer",
            ),
        ) {
            title1 { text(id(), "Vedtak om "); variable(id(), "uføretrygd") }
            outline {
                title2(id()) { text(id(), "Innledning") }
                title3(id()) { text(id(), "Mellomtittel") }
                title4(id()) { text(id(), "Detaljer") }
                paragraph(id()) {
                    text(id(), "Du får ")
                    variable(id(), "20 000 kr", FontType.BOLD)
                    newLine(id())
                    text(id(), "i måneden.", FontType.ITALIC, EditBehaviour.FRITEKST)
                    variable(id(), "fra 2026", editBehaviour = EditBehaviour.REDIGERBAR_DATA)
                }
                itemList(id()) {
                    item(id()) { text(id(), "Punkt 1") }
                    item(id()) { text(id(), "Punkt 2") }
                }
                numberedList(id()) {
                    item(id()) { text(id(), "Steg 1") }
                    item(id()) { text(id(), "Steg 2") }
                }
                table(id()) {
                    header(id()) {
                        column(id()) { text(id(), "Ytelse") }
                        column(id(), ColumnAlignment.RIGHT, span = 2) { text(id(), "Beløp "); variable(id(), "2026") }
                    }
                    row(id()) {
                        cell(id()) { text(id(), "Uføretrygd") }
                        cell(id()) { variable(id(), "20 000 kr") }
                    }
                }
                formText(id(), Size.LONG) { text(id(), "Skriv her") }
                formText(id(), Size.SHORT, vspace = false) { text(id(), "Dato") }
                formChoice(id()) {
                    prompt { text(id(), "Ønsker du å klage?") }
                    choice(id()) { text(id(), "Ja") }
                    choice(id()) { text(id(), "Nei") }
                }
            }
        }
    }

    fun request(): LetterPDFRequest {
        var next = 1000
        fun id() = next++
        return letterPDFRequest(
            letterMarkup = letter(),
            spraak = Markup.Spraak.NYNORSK,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            attachments = listOf(
                attachmentExtended(inkluderSaksinformasjon = true) {
                    title1 { text(id(), "Vedlegg 1") }
                    outline { paragraph(id()) { text(id(), "Sats: "); variable(id(), "2G") } }
                }
            ),
            pdfVedlegg = listOf(pdfTittelExtended { text(id(), "Klagerettar") }),
        )
    }

    fun withDataUsage(): LetterMarkupWithDataUsage = letterMarkupWithDataUsage(
        markup = letter(),
        brevtype = Markup.Brevtype.VEDTAKSBREV,
        letterDataUsage = setOf(
            dataUsageProperty("UngUfoerDto", "totaltUfoerePerMnd"),
            dataUsageProperty("UngUfoerDto", "virkningsdato"),
        ),
    )
}
