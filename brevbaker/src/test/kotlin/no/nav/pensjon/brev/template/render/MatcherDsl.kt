package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table

internal fun hasBlocks(matchSize: Boolean = true, builder: BlocksAssert.() -> Unit): Matcher<LetterMarkup> =
    has(LetterMarkup::blocks, BlocksAssert(matchSize).apply(builder).build())

internal fun hasAttachments(matchSize: Boolean = true, builder: AttachmentsAssert.() -> Unit): Matcher<LetterWithAttachmentsMarkup> =
    has(LetterWithAttachmentsMarkup::attachments, AttachmentsAssert(matchSize).apply(builder).build())


@DslMarker
annotation class LetterMarkupMatcherDsl

@LetterMarkupMatcherDsl
class AttachmentsAssert(private val matchSize: Boolean) {
    private val attachmentMatchers = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun attachment(builder: AttachmentAssert.() -> Unit) {
        attachmentMatchers.add(AttachmentAssert().apply(builder).build())
    }

    fun build(): Matcher<List<LetterMarkup.Attachment>> =
        if (matchSize) {
            hasSize(equalTo(attachmentMatchers.size)) and ListIndexMatcher.forList(attachmentMatchers)
        } else ListIndexMatcher.forList(attachmentMatchers)
}

class AttachmentAssert {
    private val attachmentMatchers = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun title(that: ContentAssert.() -> Unit) {
        attachmentMatchers.add(has(LetterMarkup.Attachment::title, ContentAssert().apply(that).build()))
    }

    fun blocks(matchSize: Boolean = true, that: BlocksAssert.() -> Unit) {
        attachmentMatchers.add(has(LetterMarkup.Attachment::blocks, BlocksAssert(matchSize).apply(that).build()))
    }

    fun build(): Matcher<LetterMarkup.Attachment> =
        allOf(attachmentMatchers)
}

@LetterMarkupMatcherDsl
class BlocksAssert(private val matchSize: Boolean) {
    private val blockMatchers = mutableListOf<Matcher<Block>>()

    fun paragraph(that: ContentAssert.() -> Unit) {
        blockMatchers.add(isA(Block::type, "PARAGRAPH", has(Block.Paragraph::content, ContentAssert().apply(that).build())))
    }

    fun title1(that: ContentAssert.() -> Unit) {
        blockMatchers.add(isA(Block::type, "TITLE1", has(Block.Title1::content, ContentAssert().apply(that).build())))
    }

    fun title2(that: ContentAssert.() -> Unit) {
        blockMatchers.add(isA(Block::type, "TITLE2", has(Block.Title2::content, ContentAssert().apply(that).build())))
    }

    fun build(): Matcher<List<Block>> = if (matchSize)
        hasSize(equalTo(blockMatchers.size)) and ListIndexMatcher.forList(blockMatchers)
    else ListIndexMatcher.forList(blockMatchers)
}

@LetterMarkupMatcherDsl
class ContentAssert {
    private val contentMatchers = mutableListOf<Matcher<ParagraphContent>>()

    fun variable(that: TextAssert<ParagraphContent.Text.Variable>.() -> Unit) {
        contentMatchers.add(isA(ParagraphContent::type, "VARIABLE", TextAssert<ParagraphContent.Text.Variable>().apply(that).build()))
    }

    fun variable(str: String) {
        contentMatchers.add(
            isA(ParagraphContent::type, "VARIABLE", TextAssert<ParagraphContent.Text.Variable>().apply { text(str) }.build())
        )
    }

    fun literal(that: TextAssert<ParagraphContent.Text.Literal>.() -> Unit) {
        contentMatchers.add(
            isA(ParagraphContent::type, "LITERAL", TextAssert<ParagraphContent.Text.Literal>().apply(that).build())
        )
    }

    fun literal(str: String) {
        contentMatchers.add(
            isA(ParagraphContent::type, "LITERAL", TextAssert<ParagraphContent.Text.Literal>().apply { text(str) }.build())
        )
    }

    fun newLine() {
        contentMatchers.add(isA(ParagraphContent::type, "NEW_LINE"))
    }

    fun table(that: TableAssert.() -> Unit) {
        contentMatchers.add(
            isA(ParagraphContent::type, "TABLE", TableAssert().apply(that).build())
        )
    }

    fun list(that: ListAssert.() -> Unit) {
        contentMatchers.add(isA(ParagraphContent::type, "ITEM_LIST", ListAssert().apply(that).build()))
    }

    fun build(): Matcher<List<ParagraphContent>> =
        ListIndexMatcher.forList(contentMatchers) and hasSize(equalTo(contentMatchers.size))
}

class ListAssert {
    private val itemMatchers = mutableListOf<Matcher<ItemList.Item>>()

    fun item(that: ContentAssert.() -> Unit) {
        itemMatchers.add(has(ItemList.Item::content, ContentAssert().apply(that).build()))
    }

    fun build(): Matcher<ItemList> =
        has(ItemList::items, hasSize(equalTo(itemMatchers.size)) and ListIndexMatcher.forList(itemMatchers))
}

class TableAssert {
    private val tableMatchers = mutableListOf<Matcher<Table>>()
    private val rowMatchers = mutableListOf<Matcher<Table.Row>>()

    fun header(that: HeaderAssert.() -> Unit) {
        tableMatchers.add(has(Table::header, HeaderAssert().apply(that).build()))
    }

    fun row(that: RowAssert.() -> Unit) {
        rowMatchers.add(RowAssert().apply(that).build())
    }

    fun build(): Matcher<Table> =
        allOf(tableMatchers) and
                has(Table::rows, hasSize(equalTo(rowMatchers.size)) and ListIndexMatcher.forList(rowMatchers))

    class RowAssert {
        private val cellMatchers = mutableListOf<Matcher<Table.Cell>>()
        fun cell(that: ContentAssert.() -> Unit) {
            cellMatchers.add(has(Table.Cell::text, ContentAssert().apply(that).build()))
        }
        fun build(): Matcher<Table.Row> =
            has(Table.Row::cells, hasSize(equalTo(cellMatchers.size)) and ListIndexMatcher.forList(cellMatchers))
    }

    class HeaderAssert {
        private val columnMatchers = mutableListOf<Matcher<Table.ColumnSpec>>()
        fun column(that: ContentAssert.() -> Unit) {
            columnMatchers.add(has(Table.ColumnSpec::headerContent, has(Table.Cell::text, ContentAssert().apply(that).build())))
        }
        fun build(): Matcher<Table.Header> =
            has(Table.Header::colSpec, hasSize(equalTo(columnMatchers.size)) and ListIndexMatcher.forList(columnMatchers))
    }
}

@LetterMarkupMatcherDsl
class TextAssert<T : ParagraphContent.Text> {
    private val textMatchers = mutableListOf<Matcher<String>>()

    fun text(str: String) {
        textMatchers.add(equalTo(str))
    }

    fun build(): Matcher<T> =
        has(ParagraphContent.Text::text, allOf(textMatchers))
}