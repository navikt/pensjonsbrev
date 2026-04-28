@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.ElementTags

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
        parentId: Int? = null,
        tags: Set<ElementTags> = emptySet(),
    ) = add(Edit.ParagraphContent.Text.Literal(id, text, fontType, editedText, editedFontType, tags = tags, parentId = parentId))

    fun variable(id: Int? = null, text: String, fontType: FontType = FontType.PLAIN, parentId: Int? = null, tags: Set<ElementTags> = emptySet()) =
        add(Edit.ParagraphContent.Text.Variable(id, text, fontType, parentId, tags = tags))

    fun newLine(id: Int? = null, parentId: Int? = null) = add(Edit.ParagraphContent.Text.NewLine(id, parentId))
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

    fun itemList(id: Int? = null, listType: Listetype = Listetype.PUNKTLISTE, deletedItems: Set<Int> = emptySet(), builder: ItemListBuilder.() -> Unit) {
        content += Edit.ParagraphContent.ItemList(id = id, items = ItemListBuilder().apply(builder).items, listType = listType, deletedItems = deletedItems)
    }

    fun table(id: Int? = null, builder: TableBuilder.() -> Unit) {
        val tb = TableBuilder().apply(builder)
        content += Edit.ParagraphContent.Table(
            id = id,
            rows = tb.rows,
            header = requireNotNull(tb.header) { "table must have a header" },
        )
    }
}

@EditDsl
class ItemListBuilder {
    val items = mutableListOf<Edit.ParagraphContent.ItemList.Item>()
    fun item(id: Int? = null, deletedContent: Set<Int> = emptySet(), builder: TextContentBuilder.() -> Unit = {}) {
        items += Edit.ParagraphContent.ItemList.Item(id = id, content = TextContentBuilder().apply(builder).texts, deletedContent = deletedContent)
    }
}

@EditDsl
class TableBuilder {
    var header: Edit.ParagraphContent.Table.Header? = null
    val rows = mutableListOf<Edit.ParagraphContent.Table.Row>()

    fun header(id: Int? = null, builder: HeaderBuilder.() -> Unit) {
        check(header == null) { "header already set" }
        header = Edit.ParagraphContent.Table.Header(id = id, colSpec = HeaderBuilder().apply(builder).colSpecs)
    }

    fun row(id: Int? = null, builder: RowBuilder.() -> Unit) {
        rows += Edit.ParagraphContent.Table.Row(id = id, cells = RowBuilder().apply(builder).cells)
    }
}

@EditDsl
class HeaderBuilder {
    val colSpecs = mutableListOf<Edit.ParagraphContent.Table.ColumnSpec>()
    fun colSpec(id: Int? = null, cellId: Int? = null, alignment: Edit.ParagraphContent.Table.ColumnAlignment = LEFT, span: Int = 1, builder: TextContentBuilder.() -> Unit = {}) {
        colSpecs += Edit.ParagraphContent.Table.ColumnSpec(
            id = id,
            headerContent = Edit.ParagraphContent.Table.Cell(id = cellId, text = TextContentBuilder().apply(builder).texts),
            alignment = alignment,
            span = span,
        )
    }
}

@EditDsl
class RowBuilder {
    val cells = mutableListOf<Edit.ParagraphContent.Table.Cell>()
    fun cell(id: Int? = null, builder: TextContentBuilder.() -> Unit = {}) {
        cells += Edit.ParagraphContent.Table.Cell(id = id, text = TextContentBuilder().apply(builder).texts)
    }
}

@EditDsl
class EditLetterBuilder {
    val blocks = mutableListOf<Edit.Block>()
    fun paragraph(id: Int? = null, missingFromTemplate: Boolean = false, deletedContent: Set<Int> = emptySet(), builder: ParagraphContentBuilder.() -> Unit) {
        blocks += Edit.Block.Paragraph(id = id, editable = true, content = ParagraphContentBuilder().apply(builder).content, missingFromTemplate = missingFromTemplate, deletedContent = deletedContent)
    }
    fun title1(id: Int? = null, missingFromTemplate: Boolean = false, deletedContent: Set<Int> = emptySet(), originalType: Edit.Block.Type? = null, builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title1(id = id, editable = true, content = TextContentBuilder().apply(builder).texts, missingFromTemplate = missingFromTemplate, deletedContent = deletedContent, originalType = originalType)
    }
    fun title2(id: Int? = null, missingFromTemplate: Boolean = false, deletedContent: Set<Int> = emptySet(), originalType: Edit.Block.Type? = null, builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title2(id = id, editable = true, content = TextContentBuilder().apply(builder).texts, missingFromTemplate = missingFromTemplate, deletedContent = deletedContent, originalType = originalType)
    }
    fun title3(id: Int? = null, missingFromTemplate: Boolean = false, deletedContent: Set<Int> = emptySet(), originalType: Edit.Block.Type? = null, builder: TextContentBuilder.() -> Unit) {
        blocks += Edit.Block.Title3(id = id, editable = true, content = TextContentBuilder().apply(builder).texts, missingFromTemplate = missingFromTemplate, deletedContent = deletedContent, originalType = originalType)
    }
}

fun editLetterBlocks(builder: EditLetterBuilder.() -> Unit): List<Edit.Block> =
    EditLetterBuilder().apply(builder).blocks
