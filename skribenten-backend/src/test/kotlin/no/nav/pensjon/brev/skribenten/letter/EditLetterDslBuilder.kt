@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType

@DslMarker
annotation class EditDsl

@EditDsl
abstract class TextScope {
    abstract fun add(text: Edit.ParagraphContent.Text)

    fun literal(
        id: Int? = null,
        text: String,
        fontType: FontType = FontType.PLAIN,
        editedText: String? = null,
        editedFontType: FontType? = null,
    ) = add(Edit.ParagraphContent.Text.Literal(id, text, fontType, editedText, editedFontType))

    fun variable(id: Int? = null, text: String, fontType: FontType = FontType.PLAIN) =
        add(Edit.ParagraphContent.Text.Variable(id, text, fontType))

    fun newLine(id: Int? = null) = add(Edit.ParagraphContent.Text.NewLine(id))
}

@EditDsl
class TextContentBuilder : TextScope() {
    val texts = mutableListOf<Edit.ParagraphContent.Text>()
    override fun add(text: Edit.ParagraphContent.Text) { texts += text }
}

@EditDsl
class ParagraphContentBuilder : TextScope() {
    val content = mutableListOf<Edit.ParagraphContent>()
    override fun add(text: Edit.ParagraphContent.Text) { content += text }

    fun itemList(builder: ItemListBuilder.() -> Unit) {
        content += Edit.ParagraphContent.ItemList(id = null, items = ItemListBuilder().apply(builder).items)
    }

    fun table(builder: TableBuilder.() -> Unit) {
        val tb = TableBuilder().apply(builder)
        content += Edit.ParagraphContent.Table(
            id = null,
            rows = tb.rows,
            header = requireNotNull(tb.header) { "table must have a header" },
        )
    }
}

@EditDsl
class ItemListBuilder {
    val items = mutableListOf<Edit.ParagraphContent.ItemList.Item>()
    fun item(builder: TextContentBuilder.() -> Unit = {}) {
        items += Edit.ParagraphContent.ItemList.Item(id = null, content = TextContentBuilder().apply(builder).texts)
    }
}

@EditDsl
class TableBuilder {
    var header: Edit.ParagraphContent.Table.Header? = null
    val rows = mutableListOf<Edit.ParagraphContent.Table.Row>()

    fun header(builder: HeaderBuilder.() -> Unit) {
        check(header == null) { "header already set" }
        header = Edit.ParagraphContent.Table.Header(id = null, colSpec = HeaderBuilder().apply(builder).colSpecs)
    }

    fun row(builder: RowBuilder.() -> Unit) {
        rows += Edit.ParagraphContent.Table.Row(id = null, cells = RowBuilder().apply(builder).cells)
    }
}

@EditDsl
class HeaderBuilder {
    val colSpecs = mutableListOf<Edit.ParagraphContent.Table.ColumnSpec>()
    fun colSpec(alignment: Edit.ParagraphContent.Table.ColumnAlignment = LEFT, span: Int = 1, builder: TextContentBuilder.() -> Unit = {}) {
        colSpecs += Edit.ParagraphContent.Table.ColumnSpec(
            id = null,
            headerContent = Edit.ParagraphContent.Table.Cell(id = null, text = TextContentBuilder().apply(builder).texts),
            alignment = alignment,
            span = span,
        )
    }
}

@EditDsl
class RowBuilder {
    val cells = mutableListOf<Edit.ParagraphContent.Table.Cell>()
    fun cell(builder: TextContentBuilder.() -> Unit = {}) {
        cells += Edit.ParagraphContent.Table.Cell(id = null, text = TextContentBuilder().apply(builder).texts)
    }
}

@EditDsl
class EditLetterBuilder {
    val blocks = mutableListOf<Edit.Block>()
    fun paragraph(builder: ParagraphContentBuilder.() -> Unit) {
        blocks += Edit.Block.Paragraph(id = null, editable = true, content = ParagraphContentBuilder().apply(builder).content)
    }
    fun title1(builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title1(id = null, editable = true, content = TextContentBuilder().apply(builder).texts)
    }
    fun title2(builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title2(id = null, editable = true, content = TextContentBuilder().apply(builder).texts)
    }
    fun title3(builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title3(id = null, editable = true, content = TextContentBuilder().apply(builder).texts)
    }
}
