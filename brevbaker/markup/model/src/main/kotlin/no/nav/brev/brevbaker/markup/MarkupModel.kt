package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage.Property
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

/**
 * Fabrikkflaten som lar et DSL-lag konstruere markup-modellen.
 *
 * Dette er bevisst holdt som ett navnerom med rene fabrikkfunksjoner, og ikke som public konstruktører:
 * da forblir `copy()` skjult (se [MarkupModelApi]), og hele flaten er ett symbol å slå av og på i
 * completion. Funksjonene her validerer ingenting — de er ren konstruksjon. All validering hører hjemme
 * i DSL-en.
 */
@MarkupModelApi
object MarkupModel {

    // --- Text ---

    fun literal(id: Int, text: String, fontType: Text.FontType = Text.FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Literal =
        Text.Literal(id, text, fontType, editBehaviour)

    fun variable(id: Int, text: String, fontType: Text.FontType = Text.FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Variable =
        Text.Variable(id, text, fontType, editBehaviour)

    fun newLine(id: Int): Text.NewLine = Text.NewLine(id)

    // --- Block ---

    fun title2(id: Int, content: List<Text>): Block.Title2 = Block.Title2(id, content)

    fun title3(id: Int, content: List<Text>): Block.Title3 = Block.Title3(id, content)

    fun title4(id: Int, content: List<Text>): Block.Title4 = Block.Title4(id, content)

    fun paragraph(id: Int, content: List<Text>): Block.Paragraph = Block.Paragraph(id, content)

    fun itemList(id: Int, items: List<Block.Item>): Block.ItemList = Block.ItemList(id, items)

    fun numberedList(id: Int, items: List<Block.Item>): Block.NumberedList = Block.NumberedList(id, items)

    fun item(id: Int, content: List<Text>): Block.Item = Block.Item(id, content)

    fun formText(id: Int, content: List<Text>, size: Block.FormText.Size, vspace: Boolean): Block.FormText =
        Block.FormText(id, content, size, vspace)

    fun formChoice(id: Int, prompt: List<Text>, choices: List<Block.FormChoice.Choice>, vspace: Boolean): Block.FormChoice =
        Block.FormChoice(id, prompt, choices, vspace)

    fun choice(id: Int, content: List<Text>): Block.FormChoice.Choice = Block.FormChoice.Choice(id, content)

    // --- Table ---

    fun table(id: Int, rows: List<Block.Table.Row>, header: Block.Table.Header): Block.Table = Block.Table(id, rows, header)

    fun row(id: Int, cells: List<Block.Table.Cell>): Block.Table.Row = Block.Table.Row(id, cells)

    fun cell(id: Int, content: List<Text>): Block.Table.Cell = Block.Table.Cell(id, content)

    fun header(id: Int, colSpec: List<Block.Table.ColumnSpec>): Block.Table.Header = Block.Table.Header(id, colSpec)

    fun columnSpec(id: Int, content: List<Text>, alignment: Block.Table.ColumnAlignment, span: Int): Block.Table.ColumnSpec =
        Block.Table.ColumnSpec(id, content, alignment, span)

    // --- Brev ---

    fun letterMarkup(title1: List<Text>, saksinformasjon: Saksinformasjon, blocks: List<Block>, signatur: Signatur): LetterMarkup =
        LetterMarkup(title1 = title1, saksinformasjon = saksinformasjon, blocks = blocks, signatur = signatur)

    fun attachment(title1: List<Text>, blocks: List<Block>, inkluderSaksinformasjon: Boolean): Attachment =
        Attachment(title1, blocks, inkluderSaksinformasjon)

    fun pdfTittel(title1: List<Text>): PDFTittel = PDFTittel(title1)

    fun saksinformasjon(
        gjelderNavn: String,
        gjelderPersonidentifikator: String,
        annenMottakerNavn: String?,
        saksnummer: String,
        dokumentDato: LocalDate,
    ): Saksinformasjon = Saksinformasjon(
        gjelderNavn = gjelderNavn,
        gjelderPersonidentifikator = Markup.Personidentifikator(gjelderPersonidentifikator),
        annenMottakerNavn = annenMottakerNavn,
        saksnummer = Markup.Saksnummer(saksnummer),
        dokumentDato = dokumentDato,
    )

    fun signatur(navAvsenderEnhet: String, saksbehandlerSignatur: SaksbehandlerSignatur?): Signatur =
        Signatur(saksbehandlerSignatur = saksbehandlerSignatur, navAvsenderEnhet = navAvsenderEnhet)

    fun saksbehandlerSignatur(saksbehandlerNavn: String, attesterendeSaksbehandlerNavn: String?): SaksbehandlerSignatur =
        SaksbehandlerSignatur(saksbehandlerNavn, attesterendeSaksbehandlerNavn)

    fun letterMarkupWithDataUsage(markup: LetterMarkup,
                                  letterDataUsage: Set<Property>,
                                  brevtype: Markup.Brevtype) =
        LetterMarkupWithDataUsage(markup, letterDataUsage, brevtype)
}
