package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import org.assertj.core.api.Assertions.assertThat

internal fun LetterMarkup.assertHasBlocks(matchSize: Boolean = true, builder: BlocksAssert.() -> Unit) =
    assertThat(blocks).satisfies(BlocksAssert(matchSize).apply(builder).build())

internal fun LetterWithAttachmentsMarkup.assertHasAttachments(matchSize: Boolean = true, builder: AttachmentsAssert.() -> Unit) =
    assertThat(attachments).satisfies(AttachmentsAssert(matchSize).apply(builder).build())

@DslMarker
annotation class LetterMarkupMatcherDsl

private typealias Matcher<T> = (T) -> Unit

@LetterMarkupMatcherDsl
class AttachmentsAssert(private val matchSize: Boolean) {

    private val attachmentMatchers = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun attachment(builder: AttachmentAssert.() -> Unit) {
        attachmentMatchers.add(AttachmentAssert().apply(builder).build())
    }

    fun build(): Matcher<List<LetterMarkup.Attachment>> = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(attachmentMatchers)
        }
        attachmentMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class AttachmentAssert {
    private val matchers = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun title(that: ContentAssert.() -> Unit) {
        matchers.add({ assertThat(it.title).satisfies(ContentAssert().apply(that).build()) })
    }

    fun blocks(matchSize: Boolean = true, that: BlocksAssert.() -> Unit) {
        matchers.add({ assertThat(it.blocks).satisfies(BlocksAssert(matchSize).apply(that).build()) })
    }

    fun build(): Matcher<LetterMarkup.Attachment> = { actual ->
        matchers.forEach { it(actual) }
    }
}

@LetterMarkupMatcherDsl
class BlocksAssert(private val matchSize: Boolean) {
    private val blockMatchers = mutableListOf<Matcher<Block>>()

    fun paragraph(that: ContentAssert.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.Paragraph::class.java)
            assertThat((it as Block.Paragraph).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title1(that: ContentAssert.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.Title1::class.java)
            assertThat((it as Block.Title1).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title2(that: ContentAssert.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.Title2::class.java)
            assertThat((it as Block.Title2).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title3(that: ContentAssert.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.Title3::class.java)
            assertThat((it as Block.Title3).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): Matcher<List<Block>> = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(blockMatchers)
        }
        blockMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

@LetterMarkupMatcherDsl
class ContentAssert {
    private val contentMatchers = mutableListOf<Matcher<ParagraphContent>>()

    fun variable(that: TextAssert<ParagraphContent.Text.Variable>.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(ParagraphContent.Text.Variable::class.java)
            TextAssert<ParagraphContent.Text.Variable>().apply(that).build()(it as ParagraphContent.Text.Variable)
        })
    }

    fun variable(str: String) {
        variable { text(str) }
    }

    fun literal(that: TextAssert<ParagraphContent.Text.Literal>.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(ParagraphContent.Text.Literal::class.java)
            TextAssert<ParagraphContent.Text.Literal>().apply(that).build()(it as ParagraphContent.Text.Literal)
        })
    }

    fun literal(str: String) {
        literal { text(str) }
    }

    fun newLine() {
        contentMatchers.add({
            assertThat(it.type).isEqualTo(ParagraphContent.Type.NEW_LINE)
        })
    }

    fun table(that: TableAssert.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(Table::class.java)
            TableAssert().apply(that).build()(it as Table)
        })
    }

    fun list(that: ListAssert.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(ItemList::class.java)
            ListAssert().apply(that).build()(it as ItemList)
        })
    }

    fun build(): Matcher<List<ParagraphContent>> = { actual ->
        assertThat(actual).hasSameSizeAs(contentMatchers)
        contentMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class ListAssert {
    private val itemMatchers = mutableListOf<Matcher<ItemList.Item>>()

    fun item(that: ContentAssert.() -> Unit) {
        itemMatchers.add({
            assertThat(it.content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): Matcher<ItemList> = { actual ->
        assertThat(actual.items).hasSameSizeAs(itemMatchers)
        itemMatchers.forEachIndexed { index, assertion ->
            assertThat(actual.items[index]).satisfies(assertion)
        }
    }
}

class TableAssert {
    private val tableMatchers = mutableListOf<Matcher<Table>>()
    private val rowMatchers = mutableListOf<Matcher<Table.Row>>()

    fun header(that: HeaderAssert.() -> Unit) {
        tableMatchers.add({
            assertThat(it.header).satisfies(HeaderAssert().apply(that).build())
        })
    }

    fun row(that: RowAssert.() -> Unit) {
        rowMatchers.add(RowAssert().apply(that).build())
    }

    fun build(): Matcher<Table> = { actual ->
        tableMatchers.forEach { it(actual) }
        assertThat(actual.rows).hasSameSizeAs(rowMatchers)
        rowMatchers.forEachIndexed { index, assertion ->
            assertThat(actual.rows[index]).satisfies(assertion)
        }
    }

    class RowAssert {
        private val cellMatchers = mutableListOf<Matcher<Table.Cell>>()
        fun cell(that: ContentAssert.() -> Unit) {
            cellMatchers.add({
                assertThat(it.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): Matcher<Table.Row> = { actual ->
            assertThat(actual.cells).hasSameSizeAs(cellMatchers)
            cellMatchers.forEachIndexed { index, assertion ->
                assertThat(actual.cells[index]).satisfies(assertion)
            }
        }
    }

    class HeaderAssert {
        private val columnMatchers = mutableListOf<Matcher<Table.ColumnSpec>>()
        fun column(that: ContentAssert.() -> Unit) {
            columnMatchers.add({
                assertThat(it.headerContent.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): Matcher<Table.Header> = { actual ->
            assertThat(actual.colSpec).hasSameSizeAs(columnMatchers)
            columnMatchers.forEachIndexed { index, assertion ->
                assertThat(actual.colSpec[index]).satisfies(assertion)
            }
        }
    }
}

@LetterMarkupMatcherDsl
class TextAssert<T : ParagraphContent.Text> {
    private val textMatchers = mutableListOf<Matcher<String>>()

    fun text(str: String) {
        textMatchers.add( { assertThat(it).isEqualTo(str) })
    }

    fun build(): Matcher<T> = { actual ->
        textMatchers.forEach { it(actual.text) }
    }
}