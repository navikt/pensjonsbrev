package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent.NumberedList
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.Title
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

class LetterWithAttachmentsMarkupV2Asserter(actual: LetterWithAttachmentsMarkupV2) :
    AbstractAssert<LetterWithAttachmentsMarkupV2Asserter, LetterWithAttachmentsMarkupV2>(actual, LetterWithAttachmentsMarkupV2Asserter::class.java) {

    fun hasAttachments(matchSize: Boolean = true, builder: AttachmentsAssertV2.() -> Unit) =
        assertThat(actual.attachments).satisfies(AttachmentsAssertV2(matchSize).apply(builder).build())

    companion object {
        fun assertThat(actual: LetterWithAttachmentsMarkupV2) = LetterWithAttachmentsMarkupV2Asserter(actual)
    }
}

@LetterMarkupMatcherDsl
class AttachmentsAssertV2(private val matchSize: Boolean) {
    private val attachmentMatchers = mutableListOf<(LetterMarkupV2.Attachment) -> Unit>()

    fun attachment(builder: AttachmentAssertV2.() -> Unit) {
        attachmentMatchers.add(AttachmentAssertV2().apply(builder).build())
    }

    fun build(): (List<LetterMarkupV2.Attachment>) -> Unit = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(attachmentMatchers)
        }
        attachmentMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class AttachmentAssertV2 {
    private val matchers = mutableListOf<(LetterMarkupV2.Attachment) -> Unit>()

    fun title(that: TextContentAssertV2.() -> Unit) {
        matchers.add({ assertThat(it.title1).satisfies(TextContentAssertV2().apply(that).build()) })
    }

    fun blocks(matchSize: Boolean = true, that: BlocksAssertV2.() -> Unit) {
        matchers.add({ assertThat(it.blocks).satisfies(BlocksAssertV2(matchSize).apply(that).build()) })
    }

    fun build(): (LetterMarkupV2.Attachment) -> Unit = { actual ->
        matchers.forEach { it(actual) }
    }
}

class LetterMarkupV2Asserter(actual: LetterMarkupV2) : AbstractAssert<LetterMarkupV2Asserter, LetterMarkupV2>(actual, LetterMarkupV2Asserter::class.java) {
    fun hasBlocks(matchSize: Boolean = true, builder: BlocksAssertV2.() -> Unit) =
        assertThat(actual.blocks).satisfies(BlocksAssertV2(matchSize).apply(builder).build())

    fun hasTitle1(builder: TextContentAssertV2.() -> Unit) =
        assertThat(actual.title1).satisfies(TextContentAssertV2().apply(builder).build())

    companion object {
        fun assertThat(actual: LetterMarkupV2) = LetterMarkupV2Asserter(actual)
    }
}

private typealias MatcherV2<T> = (T) -> Unit

@LetterMarkupMatcherDsl
class BlocksAssertV2(private val matchSize: Boolean) {
    private val blockMatchers = mutableListOf<MatcherV2<Block>>()

    fun paragraph(that: TextContentAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.Paragraph::class.java)
            assertThat((it as Block.Paragraph).content).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun title2(that: TextContentAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Title.Title2::class.java)
            assertThat((it as Title.Title2).content).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun title3(that: TextContentAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Title.Title3::class.java)
            assertThat((it as Title.Title3).content).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun title4(that: TextContentAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Title.Title4::class.java)
            assertThat((it as Title.Title4).content).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun list(that: ListAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(ItemList::class.java)
            ListAssertV2().apply(that).build()(it as ListContent)
        })
    }

    fun numberedList(that: ListAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(NumberedList::class.java)
            ListAssertV2().apply(that).build()(it as ListContent)
        })
    }

    fun table(that: TableAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Table::class.java)
            TableAssertV2().apply(that).build()(it as Table)
        })
    }

    fun formChoice(that: FormChoiceAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.FormChoice::class.java)
            FormChoiceAssertV2().apply(that).build()(it as Block.FormChoice)
        })
    }

    fun formText(that: FormTextAssertV2.() -> Unit) {
        blockMatchers.add({
            assertThat(it).isInstanceOf(Block.FormText::class.java)
            FormTextAssertV2().apply(that).build()(it as Block.FormText)
        })
    }

    fun build(): MatcherV2<List<Block>> = { actual ->
        if (matchSize) {
            assertThat(actual).hasSameSizeAs(blockMatchers)
        }
        blockMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

@LetterMarkupMatcherDsl
class TextContentAssertV2 {
    private val contentMatchers = mutableListOf<MatcherV2<Text>>()

    fun variable(that: TextAssertV2<Text.Variable>.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(Text.Variable::class.java)
            TextAssertV2<Text.Variable>().apply(that).build()(it as Text.Variable)
        })
    }

    fun variable(str: String) {
        variable { text(str) }
    }

    fun literal(that: TextAssertV2<Text.Literal>.() -> Unit) {
        contentMatchers.add({
            assertThat(it).isInstanceOf(Text.Literal::class.java)
            TextAssertV2<Text.Literal>().apply(that).build()(it as Text.Literal)
        })
    }

    fun literal(str: String) {
        literal { text(str) }
    }

    fun newLine() {
        contentMatchers.add({
            assertThat(it.type).isEqualTo(Text.Type.NEW_LINE)
        })
    }

    fun build(): MatcherV2<List<Text>> = { actual ->
        assertThat(actual).hasSameSizeAs(contentMatchers)
        contentMatchers.forEachIndexed { index, assertion ->
            assertThat(actual[index]).satisfies(assertion)
        }
    }
}

class ListAssertV2 {
    private val itemMatchers = mutableListOf<MatcherV2<ListContent.Item>>()

    fun item(that: TextContentAssertV2.() -> Unit) {
        itemMatchers.add({
            assertThat(it.content).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun build(): MatcherV2<ListContent> = { actual ->
        assertThat(actual.items).hasSameSizeAs(itemMatchers)
        itemMatchers.forEachIndexed { index, assertion ->
            assertThat(actual.items[index]).satisfies(assertion)
        }
    }
}

class TableAssertV2 {
    private val rowMatchers = mutableListOf<MatcherV2<Table.Row>>()
    private val tableMatchers = mutableListOf<MatcherV2<Table>>()

    fun header(that: HeaderAssertV2.() -> Unit) {
        tableMatchers.add({
            assertThat(it.header).satisfies(HeaderAssertV2().apply(that).build())
        })
    }

    fun row(that: RowAssertV2.() -> Unit) {
        rowMatchers.add(RowAssertV2().apply(that).build())
    }

    fun build(): MatcherV2<Table> = { actual ->
        tableMatchers.forEach { it(actual) }
        assertThat(actual.rows).hasSameSizeAs(rowMatchers)
        rowMatchers.forEachIndexed { index, assertion ->
            assertThat(actual.rows[index]).satisfies(assertion)
        }
    }

    class RowAssertV2 {
        private val cellMatchers = mutableListOf<MatcherV2<Table.Cell>>()
        fun cell(that: TextContentAssertV2.() -> Unit) {
            cellMatchers.add({
                assertThat(it.text).satisfies(TextContentAssertV2().apply(that).build())
            })
        }
        fun build(): MatcherV2<Table.Row> = { actual ->
            assertThat(actual.cells).hasSameSizeAs(cellMatchers)
            cellMatchers.forEachIndexed { index, assertion ->
                assertThat(actual.cells[index]).satisfies(assertion)
            }
        }
    }

    class HeaderAssertV2 {
        private val columnMatchers = mutableListOf<MatcherV2<Table.ColumnSpec>>()
        fun column(that: TextContentAssertV2.() -> Unit) {
            columnMatchers.add({
                assertThat(it.headerContent.text).satisfies(TextContentAssertV2().apply(that).build())
            })
        }
        fun build(): MatcherV2<Table.Header> = { actual ->
            assertThat(actual.colSpec).hasSameSizeAs(columnMatchers)
            columnMatchers.forEachIndexed { index, assertion ->
                assertThat(actual.colSpec[index]).satisfies(assertion)
            }
        }
    }
}

@LetterMarkupMatcherDsl
class TextAssertV2<T : Text> {
    private val textMatchers = mutableListOf<MatcherV2<String>>()

    fun text(str: String) {
        textMatchers.add({ assertThat(it).isEqualTo(str) })
    }

    fun build(): MatcherV2<T> = { actual ->
        textMatchers.forEach { it(actual.text) }
    }
}

class FormChoiceAssertV2 {
    private var promptMatcher: MatcherV2<List<Text>>? = null
    private val choiceMatchers = mutableListOf<MatcherV2<Block.FormChoice.Choice>>()

    fun prompt(that: TextContentAssertV2.() -> Unit) {
        promptMatcher = TextContentAssertV2().apply(that).build()
    }

    fun choice(that: TextContentAssertV2.() -> Unit) {
        choiceMatchers.add({
            assertThat(it.text).satisfies(TextContentAssertV2().apply(that).build())
        })
    }

    fun build(): MatcherV2<Block.FormChoice> = { actual ->
        promptMatcher?.let { assertThat(actual.prompt).satisfies(it) }
        assertThat(actual.choices).hasSameSizeAs(choiceMatchers)
        choiceMatchers.forEachIndexed { index, assertion ->
            assertThat(actual.choices[index]).satisfies(assertion)
        }
    }
}

class FormTextAssertV2 {
    private var promptMatcher: MatcherV2<List<Text>>? = null

    fun prompt(that: TextContentAssertV2.() -> Unit) {
        promptMatcher = TextContentAssertV2().apply(that).build()
    }

    fun build(): MatcherV2<Block.FormText> = { actual ->
        promptMatcher?.let { assertThat(actual.prompt).satisfies(it) }
    }
}
