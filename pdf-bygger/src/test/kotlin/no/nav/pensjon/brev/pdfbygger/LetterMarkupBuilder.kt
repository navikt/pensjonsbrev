@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import java.time.LocalDate

fun letterMarkup(block: LetterMarkupBuilder.() -> Unit): LetterMarkup =
    LetterMarkupBuilder().apply(block).build()

fun attachment(block: AttachmentBuilder.() -> Unit): LetterMarkup.Attachment {
    return AttachmentBuilder().apply(block).build()
}


@DslMarker
annotation class LetterMarkupBuilderDsl

@LetterMarkupBuilderDsl
class LetterMarkupBuilder {
    private var title = emptyList<ParagraphContent.Text>()
    private var blocks = emptyList<LetterMarkup.Block>()
    private var sakspart = SakspartBuilder().build()
    private var signatur = SignaturBuilder().build()

    fun title(block: TextBuilder.() -> Unit) {
        title = TextBuilder().apply(block).build()
    }

    fun outline(block: LetterMarkupBlocksBuilder.() -> Unit) {
        blocks = LetterMarkupBlocksBuilder().apply(block).build()
    }

    fun sakspart(block: SakspartBuilder.() -> Unit) {
        sakspart = SakspartBuilder().apply(block).build()
    }

    fun signatur(block: SignaturBuilder.() -> Unit) {
        signatur = SignaturBuilder().apply(block).build()
    }

    fun build(): LetterMarkup {
        return LetterMarkupImpl(
            title = title,
            sakspart = sakspart,
            blocks = blocks,
            signatur = signatur
        )
    }
}

@LetterMarkupBuilderDsl
class SakspartBuilder {
    var gjelderNavn: String = "Navn Navnesen"
    var gjelderFoedselsnummer: Foedselsnummer = Foedselsnummer("12345678901")
    var annenMottakerNavn: String? = null
    var vergeNavn: String? = null
    var saksnummer: String = "123"
    var dokumentDato: LocalDate = LocalDate.of(2025, 1, 1)

    fun build(): LetterMarkup.Sakspart =
        LetterMarkupImpl.SakspartImpl(
            gjelderNavn = gjelderNavn,
            gjelderFoedselsnummer = gjelderFoedselsnummer,
            annenMottakerNavn = annenMottakerNavn,
            vergeNavn = vergeNavn,
            saksnummer = saksnummer,
            dokumentDato = dokumentDato,
        )
}

@LetterMarkupBuilderDsl
class SignaturBuilder {
    var hilsenTekst: String = "hilsen"
    var saksbehandlerNavn: String = "Saksbehandler Saksbehandlersen"
    var attesterendeSaksbehandlerNavn: String? = null
    var navAvsenderEnhet: String = "Nav sentralt"

    fun build(): LetterMarkup.Signatur =
        LetterMarkupImpl.SignaturImpl(
            hilsenTekst = hilsenTekst,
            saksbehandlerNavn = saksbehandlerNavn,
            attesterendeSaksbehandlerNavn = attesterendeSaksbehandlerNavn,
            navAvsenderEnhet = navAvsenderEnhet,
        )
}

@LetterMarkupBuilderDsl
class AttachmentBuilder {
    private var title = emptyList<ParagraphContent.Text>()
    private var blocks = emptyList<LetterMarkup.Block>()
    var includeSakspart = false

    fun title(block: TextBuilder.() -> Unit) {
        title = TextBuilder().apply(block).build()
    }

    fun outline(block: LetterMarkupBlocksBuilder.() -> Unit) {
        blocks = LetterMarkupBlocksBuilder().apply(block).build()
    }

    fun build(): LetterMarkup.Attachment =
        LetterMarkupImpl.AttachmentImpl(
            title = title,
            blocks = blocks,
            includeSakspart = includeSakspart,
        )
}

@LetterMarkupBuilderDsl
class LetterMarkupBlocksBuilder {
    private val blocks = mutableListOf<LetterMarkup.Block>()

    fun title1(block: TextBuilder.() -> Unit) {
        val content = TextBuilder().apply(block).build()
        blocks.add(
            LetterMarkupImpl.BlockImpl.Title1Impl(
                id = content.fold(1) { hash, e -> 31 * hash + (e.id) },
                content = content,
            )
        )
    }

    fun title2(block: TextBuilder.() -> Unit) {
        val content = TextBuilder().apply(block).build()
        blocks.add(
            LetterMarkupImpl.BlockImpl.Title2Impl(
                id = content.fold(1) { hash, e -> 31 * hash + (e.id) },
                content = content,
            )
        )
    }

    fun paragraph(block: ParagraphBuilder.() -> Unit) {
        blocks.add(ParagraphBuilder().apply(block).build())
    }

    fun build(): List<LetterMarkup.Block> = blocks
}


@LetterMarkupBuilderDsl
class TextBuilder {
    private val texts = mutableListOf<ParagraphContent.Text>()

    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        texts.add(LiteralImpl(text.hashCode(), text, fontType))
    }

    fun newLine() {
        texts.add(LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(1))
    }

    fun build(): List<ParagraphContent.Text> = texts
}

@LetterMarkupBuilderDsl
class ParagraphBuilder {
    private val content = mutableListOf<ParagraphContent>()

    fun text(text: String) {
        content.add(LiteralImpl(text.hashCode(), text))
    }

    fun newLine() {
        content.add(LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(1))
    }

    fun list(block: ItemListBuilder.() -> Unit) {
        content.add(ItemListBuilder().apply(block).build())
    }

    fun table(header: TableHeaderBuilder.() -> Unit, bodyBlock: TableBodyBuilder.() -> Unit) {
        val header = TableHeaderBuilder().apply(header).build()
        val body = TableBodyBuilder().apply(bodyBlock).build()

        content.add(
            LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                id = body.fold(header.id) { hash, e -> 31 * hash + (e.id) },
                header = header,
                rows = body,
            )
        )
    }

    fun build(): LetterMarkup.Block.Paragraph =
        LetterMarkupImpl.BlockImpl.ParagraphImpl(
            id = content.fold(1) { hash, e -> 31 * hash + (e.hashCode()) },
            content = content
        )
}

@LetterMarkupBuilderDsl
class ItemListBuilder {
    private val items = mutableListOf<ParagraphContent.ItemList.Item>()

    fun item(block: TextBuilder.() -> Unit) {
        val text = TextBuilder().apply(block).build()
        items.add(
            LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl(
                id = text.fold(1) { hash, e -> 31 * hash + (e.id) },
                content = text
            )
        )
    }

    fun build(): ParagraphContent.ItemList =
        LetterMarkupImpl.ParagraphContentImpl.ItemListImpl(
            id = items.fold(1) { hash, e -> 31 * hash + (e.id) },
            items = items
        )
}

@LetterMarkupBuilderDsl
class TableHeaderBuilder {
    private val columns = mutableListOf<ParagraphContent.Table.ColumnSpec>()

    fun column(
        span: Int = 1,
        alignment: ParagraphContent.Table.ColumnAlignment = ParagraphContent.Table.ColumnAlignment.LEFT,
        block: TextBuilder.() -> Unit
    ) {
        val text = TextBuilder().apply(block).build()
        val id = text.fold(1) { hash, e -> 31 * hash + (e.id) }

        columns.add(
            LetterMarkupImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                id = id,
                headerContent = LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                    id = id,
                    text = text,
                ),
                alignment = alignment,
                span = span,
            )
        )
    }

    fun build(): ParagraphContent.Table.Header =
        LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
            id = columns.fold(1) { hash, e -> 31 * hash + (e.id) },
            colSpec = columns,
        )
}

@LetterMarkupBuilderDsl
class TableBodyBuilder {
    private val rows = mutableListOf<ParagraphContent.Table.Row>()

    fun row(block: TableRowBuilder.() -> Unit) {
        rows.add(TableRowBuilder().apply(block).build())
    }

    fun build(): List<ParagraphContent.Table.Row> = rows
}

@LetterMarkupBuilderDsl
class TableRowBuilder {
    private val cells = mutableListOf<ParagraphContent.Table.Cell>()

    fun cell(block: TextBuilder.() -> Unit) {
        val text = TextBuilder().apply(block).build()
        val id = text.fold(1) { hash, e -> 31 * hash + (e.id) }

        cells.add(
            LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                id = id,
                text = text,
            )
        )
    }

    fun build(): ParagraphContent.Table.Row =
        LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl(
            id = cells.fold(1) { hash, e -> 31 * hash + (e.id) },
            cells = cells,
        )
}