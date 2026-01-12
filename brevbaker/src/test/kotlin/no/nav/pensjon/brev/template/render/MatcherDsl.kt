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

    private val attachmentAssertions = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun attachment(builder: AttachmentAssert.() -> Unit) {
        attachmentAssertions.add(AttachmentAssert().apply(builder).build())
    }

    fun build(): Matcher<List<LetterMarkup.Attachment>> = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(attachmentAssertions)
        }
        attachmentAssertions.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class AttachmentAssert {
    private val assertions = mutableListOf<Matcher<LetterMarkup.Attachment>>()

    fun title(that: ContentAssert.() -> Unit) {
        assertions.add({ assertThat(it.title).satisfies(ContentAssert().apply(that).build()) })
    }

    fun blocks(matchSize: Boolean = true, that: BlocksAssert.() -> Unit) {
        assertions.add({ assertThat(it.blocks).satisfies(BlocksAssert(matchSize).apply(that).build()) })
    }

    fun build(): Matcher<LetterMarkup.Attachment> = { actual ->
        assertions.forEach { it(actual) }
    }
}

@LetterMarkupMatcherDsl
class BlocksAssert(private val matchSize: Boolean) {
    private val blockAssertions = mutableListOf<Matcher<Block>>()

    fun paragraph(that: ContentAssert.() -> Unit) {
        blockAssertions.add({
            assertThat(it).isInstanceOf(Block.Paragraph::class.java)
            assertThat((it as Block.Paragraph).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title1(that: ContentAssert.() -> Unit) {
        blockAssertions.add({
            assertThat(it).isInstanceOf(Block.Title1::class.java)
            assertThat((it as Block.Title1).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title2(that: ContentAssert.() -> Unit) {
        blockAssertions.add({
            assertThat(it).isInstanceOf(Block.Title2::class.java)
            assertThat((it as Block.Title2).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title3(that: ContentAssert.() -> Unit) {
        blockAssertions.add({
            assertThat(it).isInstanceOf(Block.Title3::class.java)
            assertThat((it as Block.Title3).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): Matcher<List<Block>> = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(blockAssertions)
        }
        blockAssertions.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

@LetterMarkupMatcherDsl
class ContentAssert {
    private val contentAssertions = mutableListOf<(ParagraphContent) -> Unit>()

    fun variable(that: TextAssert<ParagraphContent.Text.Variable>.() -> Unit) {
        contentAssertions.add({
            assertThat(it).isInstanceOf(ParagraphContent.Text.Variable::class.java)
            TextAssert<ParagraphContent.Text.Variable>().apply(that).build()(it as ParagraphContent.Text.Variable)
        })
    }

    fun variable(str: String) {
        variable { text(str) }
    }

    fun literal(that: TextAssert<ParagraphContent.Text.Literal>.() -> Unit) {
        contentAssertions.add({
            assertThat(it).isInstanceOf(ParagraphContent.Text.Literal::class.java)
            TextAssert<ParagraphContent.Text.Literal>().apply(that).build()(it as ParagraphContent.Text.Literal)
        })
    }

    fun literal(str: String) {
        literal { text(str) }
    }

    fun newLine() {
        contentAssertions.add({
            assertThat(it.type).isEqualTo(ParagraphContent.Type.NEW_LINE)
        })
    }

    fun table(that: TableAssert.() -> Unit) {
        contentAssertions.add({
            assertThat(it).isInstanceOf(Table::class.java)
            TableAssert().apply(that).build()(it as Table)
        })
    }

    fun list(that: ListAssert.() -> Unit) {
        contentAssertions.add({
            assertThat(it).isInstanceOf(ItemList::class.java)
            ListAssert().apply(that).build()(it as ItemList)
        })
    }

    fun build(): (List<ParagraphContent>) -> Unit = { actual ->
        assertThat(actual).hasSameSizeAs(contentAssertions)
        contentAssertions.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class ListAssert {
    private val itemAssertions = mutableListOf<(ItemList.Item) -> Unit>()

    fun item(that: ContentAssert.() -> Unit) {
        itemAssertions.add({
            assertThat(it.content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): (ItemList) -> Unit = { actual ->
        assertThat(actual.items).hasSameSizeAs(itemAssertions)
        itemAssertions.forEachIndexed { index, assertion ->
            assertThat(actual.items[index]).satisfies(assertion)
        }
    }
}

class TableAssert {
    private val tableAssertions = mutableListOf<(Table) -> Unit>()
    private val rowAssertions = mutableListOf<(Table.Row) -> Unit>()

    fun header(that: HeaderAssert.() -> Unit) {
        tableAssertions.add({
            assertThat(it.header).satisfies(HeaderAssert().apply(that).build())
        })
    }

    fun row(that: RowAssert.() -> Unit) {
        rowAssertions.add(RowAssert().apply(that).build())
    }

    fun build(): (Table) -> Unit = { actual ->
        tableAssertions.forEach { it(actual) }
        assertThat(actual.rows).hasSameSizeAs(rowAssertions)
        rowAssertions.forEachIndexed { index, assertion ->
            assertThat(actual.rows[index]).satisfies(assertion)
        }
    }

    class RowAssert {
        private val cellAssertions = mutableListOf<(Table.Cell) -> Unit>()
        fun cell(that: ContentAssert.() -> Unit) {
            cellAssertions.add({
                assertThat(it.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): (Table.Row) -> Unit = { actual ->
            assertThat(actual.cells).hasSameSizeAs(cellAssertions)
            cellAssertions.forEachIndexed { index, assertion ->
                assertThat(actual.cells[index]).satisfies(assertion)
            }
        }
    }

    class HeaderAssert {
        private val columnAssertions = mutableListOf<(Table.ColumnSpec) -> Unit>()
        fun column(that: ContentAssert.() -> Unit) {
            columnAssertions.add({
                assertThat(it.headerContent.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): (Table.Header) -> Unit = { actual ->
            assertThat(actual.colSpec).hasSameSizeAs(columnAssertions)
            columnAssertions.forEachIndexed { index, assertion ->
                assertThat(actual.colSpec[index]).satisfies(assertion)
            }
        }
    }
}

@LetterMarkupMatcherDsl
class TextAssert<T : ParagraphContent.Text> {
    private val textAssertions = mutableListOf<(String) -> Unit>()

    fun text(str: String) {
        textAssertions.add( { assertThat(it).isEqualTo(str) })
    }

    fun build(): (T) -> Unit = { actual ->
        textAssertions.forEach { it(actual.text) }
    }
}