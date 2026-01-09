package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import org.assertj.core.api.Assertions.assertThat
import java.util.function.Consumer

internal fun hasBlocks(matchSize: Boolean = true, builder: BlocksAssert.() -> Unit): ((LetterMarkup) -> Unit) =
    { assertThat(it.blocks).satisfies(BlocksAssert(matchSize).apply(builder).build()) }

internal fun hasAttachments(matchSize: Boolean = true, builder: AttachmentsAssert.() -> Unit): ((LetterWithAttachmentsMarkup) -> Unit) =
    { assertThat(it.attachments).satisfies(AttachmentsAssert(matchSize).apply(builder).build()) }

@DslMarker
annotation class LetterMarkupMatcherDsl

@LetterMarkupMatcherDsl
class AttachmentsAssert(private val matchSize: Boolean) {
    private val attachmentAssertions = mutableListOf<Consumer<LetterMarkup.Attachment>>()

    fun attachment(builder: AttachmentAssert.() -> Unit) {
        attachmentAssertions.add(AttachmentAssert().apply(builder).build())
    }

    fun build(): Consumer<List<LetterMarkup.Attachment>> = Consumer { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(attachmentAssertions)
        }
        attachmentAssertions.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class AttachmentAssert {
    private val assertions = mutableListOf<Consumer<LetterMarkup.Attachment>>()

    fun title(that: ContentAssert.() -> Unit) {
        assertions.add(Consumer { assertThat(it.title).satisfies(ContentAssert().apply(that).build()) })
    }

    fun blocks(matchSize: Boolean = true, that: BlocksAssert.() -> Unit) {
        assertions.add(Consumer { assertThat(it.blocks).satisfies(BlocksAssert(matchSize).apply(that).build()) })
    }

    fun build(): Consumer<LetterMarkup.Attachment> = Consumer { actual ->
        assertions.forEach { it.accept(actual) }
    }
}

@LetterMarkupMatcherDsl
class BlocksAssert(private val matchSize: Boolean) {
    private val blockAssertions = mutableListOf<Consumer<Block>>()

    fun paragraph(that: ContentAssert.() -> Unit) {
        blockAssertions.add(Consumer {
            assertThat(it).isInstanceOf(Block.Paragraph::class.java)
            assertThat((it as Block.Paragraph).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title1(that: ContentAssert.() -> Unit) {
        blockAssertions.add(Consumer {
            assertThat(it).isInstanceOf(Block.Title1::class.java)
            assertThat((it as Block.Title1).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title2(that: ContentAssert.() -> Unit) {
        blockAssertions.add(Consumer {
            assertThat(it).isInstanceOf(Block.Title2::class.java)
            assertThat((it as Block.Title2).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun title3(that: ContentAssert.() -> Unit) {
        blockAssertions.add(Consumer {
            assertThat(it).isInstanceOf(Block.Title3::class.java)
            assertThat((it as Block.Title3).content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): Consumer<List<Block>> = Consumer { actual ->
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
    private val contentAssertions = mutableListOf<Consumer<ParagraphContent>>()

    fun variable(that: TextAssert<ParagraphContent.Text.Variable>.() -> Unit) {
        contentAssertions.add(Consumer {
            assertThat(it).isInstanceOf(ParagraphContent.Text.Variable::class.java)
            TextAssert<ParagraphContent.Text.Variable>().apply(that).build().accept(it as ParagraphContent.Text.Variable)
        })
    }

    fun variable(str: String) {
        variable { text(str) }
    }

    fun literal(that: TextAssert<ParagraphContent.Text.Literal>.() -> Unit) {
        contentAssertions.add(Consumer {
            assertThat(it).isInstanceOf(ParagraphContent.Text.Literal::class.java)
            TextAssert<ParagraphContent.Text.Literal>().apply(that).build().accept(it as ParagraphContent.Text.Literal)
        })
    }

    fun literal(str: String) {
        literal { text(str) }
    }

    fun newLine() {
        contentAssertions.add(Consumer {
            assertThat(it.type).isEqualTo(ParagraphContent.Type.NEW_LINE)
        })
    }

    fun table(that: TableAssert.() -> Unit) {
        contentAssertions.add(Consumer {
            assertThat(it).isInstanceOf(Table::class.java)
            TableAssert().apply(that).build().accept(it as Table)
        })
    }

    fun list(that: ListAssert.() -> Unit) {
        contentAssertions.add(Consumer {
            assertThat(it).isInstanceOf(ItemList::class.java)
            ListAssert().apply(that).build().accept(it as ItemList)
        })
    }

    fun build(): Consumer<List<ParagraphContent>> = Consumer { actual ->
        assertThat(actual).hasSameSizeAs(contentAssertions)
        contentAssertions.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class ListAssert {
    private val itemAssertions = mutableListOf<Consumer<ItemList.Item>>()

    fun item(that: ContentAssert.() -> Unit) {
        itemAssertions.add(Consumer {
            assertThat(it.content).satisfies(ContentAssert().apply(that).build())
        })
    }

    fun build(): Consumer<ItemList> = Consumer { actual ->
        assertThat(actual.items).hasSameSizeAs(itemAssertions)
        itemAssertions.forEachIndexed { index, assertion ->
            assertThat(actual.items[index]).satisfies(assertion)
        }
    }
}

class TableAssert {
    private val tableAssertions = mutableListOf<Consumer<Table>>()
    private val rowAssertions = mutableListOf<Consumer<Table.Row>>()

    fun header(that: HeaderAssert.() -> Unit) {
        tableAssertions.add(Consumer {
            assertThat(it.header).satisfies(HeaderAssert().apply(that).build())
        })
    }

    fun row(that: RowAssert.() -> Unit) {
        rowAssertions.add(RowAssert().apply(that).build())
    }

    fun build(): Consumer<Table> = Consumer { actual ->
        tableAssertions.forEach { it.accept(actual) }
        assertThat(actual.rows).hasSameSizeAs(rowAssertions)
        rowAssertions.forEachIndexed { index, assertion ->
            assertThat(actual.rows[index]).satisfies(assertion)
        }
    }

    class RowAssert {
        private val cellAssertions = mutableListOf<Consumer<Table.Cell>>()
        fun cell(that: ContentAssert.() -> Unit) {
            cellAssertions.add(Consumer {
                assertThat(it.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): Consumer<Table.Row> = Consumer { actual ->
            assertThat(actual.cells).hasSameSizeAs(cellAssertions)
            cellAssertions.forEachIndexed { index, assertion ->
                assertThat(actual.cells[index]).satisfies(assertion)
            }
        }
    }

    class HeaderAssert {
        private val columnAssertions = mutableListOf<Consumer<Table.ColumnSpec>>()
        fun column(that: ContentAssert.() -> Unit) {
            columnAssertions.add(Consumer {
                assertThat(it.headerContent.text).satisfies(ContentAssert().apply(that).build())
            })
        }
        fun build(): Consumer<Table.Header> = Consumer { actual ->
            assertThat(actual.colSpec).hasSameSizeAs(columnAssertions)
            columnAssertions.forEachIndexed { index, assertion ->
                assertThat(actual.colSpec[index]).satisfies(assertion)
            }
        }
    }
}

@LetterMarkupMatcherDsl
class TextAssert<T : ParagraphContent.Text> {
    private val textAssertions = mutableListOf<Consumer<String>>()

    fun text(str: String) {
        textAssertions.add(Consumer { assertThat(it).isEqualTo(str) })
    }

    fun build(): Consumer<T> = Consumer { actual ->
        textAssertions.forEach { it.accept(actual.text) }
    }
}