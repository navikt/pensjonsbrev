@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.BlockImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl.LiteralImpl
import java.time.LocalDate

fun letterMarkupV2(block: LetterMarkupV2Builder.() -> Unit): LetterMarkupV2 =
    LetterMarkupV2Builder().apply(block).build()

fun attachmentV2(block: AttachmentV2Builder.() -> Unit): LetterMarkupV2.Attachment =
    AttachmentV2Builder().apply(block).build()

@DslMarker
annotation class LetterMarkupV2BuilderDsl

@LetterMarkupV2BuilderDsl
class LetterMarkupV2Builder {
    private var title1 = emptyList<Text>()
    private var blocks = emptyList<Block>()
    private var saksinformasjon = SaksinformasjonV2Builder().build()
    private var signatur = SignaturV2Builder().build()

    fun title1(block: TextV2Builder.() -> Unit) {
        title1 = TextV2Builder().apply(block).build()
    }

    fun outline(block: LetterMarkupV2BlocksBuilder.() -> Unit) {
        blocks = LetterMarkupV2BlocksBuilder().apply(block).build()
    }

    fun saksinformasjon(block: SaksinformasjonV2Builder.() -> Unit) {
        saksinformasjon = SaksinformasjonV2Builder().apply(block).build()
    }

    fun signatur(block: SignaturV2Builder.() -> Unit) {
        signatur = SignaturV2Builder().apply(block).build()
    }

    fun build(): LetterMarkupV2 =
        LetterMarkupV2Impl(
            title1 = title1,
            saksinformasjon = saksinformasjon,
            blocks = blocks,
            signatur = signatur,
        )
}

@LetterMarkupV2BuilderDsl
class SaksinformasjonV2Builder {
    var gjelderNavn: String = "Navn Navnesen"
    var gjelderFoedselsnummer: Foedselsnummer = Foedselsnummer("12345678901")
    var annenMottakerNavn: String? = null
    var saksnummer: String = "123"
    var dokumentDato: LocalDate = LocalDate.of(2025, 1, 1)

    fun build(): LetterMarkupV2.Saksinformasjon =
        LetterMarkupV2Impl.SaksinformasjonImpl(
            gjelderNavn = gjelderNavn,
            gjelderFoedselsnummer = gjelderFoedselsnummer,
            annenMottakerNavn = annenMottakerNavn,
            saksnummer = LetterMarkupV2.Saksnummer(saksnummer),
            dokumentDato = dokumentDato,
        )
}

@LetterMarkupV2BuilderDsl
class SignaturV2Builder {
    var hilsenTekst: String = "hilsen"
    var saksbehandlerNavn: String? = "Saksbehandler Saksbehandlersen"
    var attesterendeSaksbehandlerNavn: String? = null
    var navAvsenderEnhet: String = "Nav sentralt"

    fun build(): LetterMarkupV2.Signatur =
        LetterMarkupV2Impl.SignaturImpl(
            hilsenTekst = hilsenTekst,
            saksbehandlerSignatur = saksbehandlerNavn?.let {
                LetterMarkupV2Impl.SaksbehandlerSignaturImpl(
                    saksbehandlerNavn = it,
                    attesterendeSaksbehandlerNavn = attesterendeSaksbehandlerNavn,
                )
            },
            navAvsenderEnhet = navAvsenderEnhet,
        )
}

@LetterMarkupV2BuilderDsl
class AttachmentV2Builder {
    private var title1 = emptyList<Text>()
    private var blocks = emptyList<Block>()
    var inkluderSaksinformasjon = false

    fun title1(block: TextV2Builder.() -> Unit) {
        title1 = TextV2Builder().apply(block).build()
    }

    fun outline(block: LetterMarkupV2BlocksBuilder.() -> Unit) {
        blocks = LetterMarkupV2BlocksBuilder().apply(block).build()
    }

    fun build(): LetterMarkupV2.Attachment =
        LetterMarkupV2Impl.AttachmentImpl(
            title1 = title1,
            blocks = blocks,
            inkluderSaksinformasjon = inkluderSaksinformasjon,
        )
}

@LetterMarkupV2BuilderDsl
class LetterMarkupV2BlocksBuilder {
    private val blocks = mutableListOf<Block>()

    fun title2(block: TextV2Builder.() -> Unit) {
        val content = TextV2Builder().apply(block).build()
        blocks.add(BlockImpl.Title2Impl(id = content.fold(1) { hash, e -> 31 * hash + e.id }, content = content))
    }

    fun title3(block: TextV2Builder.() -> Unit) {
        val content = TextV2Builder().apply(block).build()
        blocks.add(BlockImpl.Title3Impl(id = content.fold(1) { hash, e -> 31 * hash + e.id }, content = content))
    }

    fun title4(block: TextV2Builder.() -> Unit) {
        val content = TextV2Builder().apply(block).build()
        blocks.add(BlockImpl.Title4Impl(id = content.fold(1) { hash, e -> 31 * hash + e.id }, content = content))
    }

    fun paragraph(block: TextV2Builder.() -> Unit) {
        val content = TextV2Builder().apply(block).build()
        blocks.add(BlockImpl.ParagraphImpl(id = content.fold(1) { hash, e -> 31 * hash + e.id }, content = content))
    }

    fun list(block: ItemListV2Builder.() -> Unit) {
        blocks.add(ItemListV2Builder().apply(block).build())
    }

    fun numberedList(block: ItemListV2Builder.() -> Unit) {
        blocks.add(ItemListV2Builder().apply(block).buildNumbered())
    }

    fun table(header: TableHeaderV2Builder.() -> Unit, bodyBlock: TableBodyV2Builder.() -> Unit) {
        val builtHeader = TableHeaderV2Builder().apply(header).build()
        val body = TableBodyV2Builder().apply(bodyBlock).build()

        blocks.add(
            BlockImpl.TableImpl(
                id = body.fold(builtHeader.id) { hash, e -> 31 * hash + e.id },
                rows = body,
                header = builtHeader,
            )
        )
    }

    fun build(): List<Block> = blocks
}

@LetterMarkupV2BuilderDsl
class TextV2Builder {
    private val texts = mutableListOf<Text>()

    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        texts.add(LiteralImpl(text.hashCode(), text, fontType))
    }

    fun newLine() {
        texts.add(LetterMarkupV2Impl.TextImpl.NewLineImpl(1))
    }

    fun build(): List<Text> = texts
}

@LetterMarkupV2BuilderDsl
class ItemListV2Builder {
    private val items = mutableListOf<Block.ListContent.Item>()

    fun item(block: TextV2Builder.() -> Unit) {
        val text = TextV2Builder().apply(block).build()
        items.add(BlockImpl.ItemListImpl.ItemImpl(id = text.fold(1) { hash, e -> 31 * hash + e.id }, content = text))
    }

    fun build(): Block.ListContent.ItemList =
        BlockImpl.ItemListImpl(id = items.fold(1) { hash, e -> 31 * hash + e.id }, items = items)

    fun buildNumbered(): Block.ListContent.NumberedList =
        BlockImpl.NumberedListImpl(id = items.fold(1) { hash, e -> 31 * hash + e.id }, items = items)
}

@LetterMarkupV2BuilderDsl
class TableHeaderV2Builder {
    private val columns = mutableListOf<Block.Table.ColumnSpec>()

    fun column(
        span: Int = 1,
        alignment: Block.Table.ColumnAlignment = Block.Table.ColumnAlignment.LEFT,
        block: TextV2Builder.() -> Unit,
    ) {
        val text = TextV2Builder().apply(block).build()
        val id = text.fold(1) { hash, e -> 31 * hash + e.id }

        columns.add(
            BlockImpl.TableImpl.ColumnSpecImpl(
                id = id,
                headerContent = BlockImpl.TableImpl.CellImpl(id = id, text = text),
                alignment = alignment,
                span = span,
            )
        )
    }

    fun build(): Block.Table.Header =
        BlockImpl.TableImpl.HeaderImpl(id = columns.fold(1) { hash, e -> 31 * hash + e.id }, colSpec = columns)
}

@LetterMarkupV2BuilderDsl
class TableBodyV2Builder {
    private val rows = mutableListOf<Block.Table.Row>()

    fun row(block: TableRowV2Builder.() -> Unit) {
        rows.add(TableRowV2Builder().apply(block).build())
    }

    fun build(): List<Block.Table.Row> = rows
}

@LetterMarkupV2BuilderDsl
class TableRowV2Builder {
    private val cells = mutableListOf<Block.Table.Cell>()

    fun cell(block: TextV2Builder.() -> Unit) {
        val text = TextV2Builder().apply(block).build()
        val id = text.fold(1) { hash, e -> 31 * hash + e.id }
        cells.add(BlockImpl.TableImpl.CellImpl(id = id, text = text))
    }

    fun build(): Block.Table.Row =
        BlockImpl.TableImpl.RowImpl(id = cells.fold(1) { hash, e -> 31 * hash + e.id }, cells = cells)
}
