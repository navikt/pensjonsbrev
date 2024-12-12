package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.*
import no.nav.pensjon.brev.skribenten.letter.Edit.Letter
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList.Item
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable

val Letter.literals: Sequence<Literal>
    get() = object : EditLetterSequence<Literal>() {
        override suspend fun SequenceScope<Literal>.visit(content: Literal) {
            yield(content)
        }
    }.build(this)

val Letter.variables: Sequence<Variable>
    get() = object : EditLetterSequence<Variable>() {
        override suspend fun SequenceScope<Variable>.visit(content: Variable) {
            yield(content)
        }
    }.build(this)


abstract class EditLetterSequence<T> {
    fun build(letter: Letter): Sequence<T> = sequence {
        visit(letter)
    }

    open suspend fun SequenceScope<T>.visit(letter: Letter): Unit =
        letter.blocks.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(block: Block): Unit =
        when (block) {
            is Title1 -> visit(block)
            is Title2 -> visit(block)
            is Paragraph -> visit(block)
        }

    open suspend fun SequenceScope<T>.visit(block: Title1): Unit =
        block.content.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(block: Paragraph): Unit =
        block.content.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(block: Title2): Unit =
        block.content.forEach { visit(it) }


    open suspend fun SequenceScope<T>.visit(content: ParagraphContent): Unit =
        when (content) {
            is ItemList -> visit(content)
            is Table -> visit(content)
            is Text -> visit(content)
        }

    open suspend fun SequenceScope<T>.visit(content: Text): Unit =
        when (content) {
            is Literal -> visit(content)
            is Variable -> visit(content)
        }

    open suspend fun SequenceScope<T>.visit(content: Literal): Unit = Unit
    open suspend fun SequenceScope<T>.visit(content: Variable): Unit = Unit

    open suspend fun SequenceScope<T>.visit(itemList: ItemList): Unit =
        itemList.items.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(item: Item): Unit =
        item.content.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(table: Table) {
        visit(table.header)
        table.rows.forEach { visit(it) }
    }

    open suspend fun SequenceScope<T>.visit(header: Header): Unit =
        header.colSpec.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(colSpec: ColumnSpec): Unit =
        visit(colSpec.headerContent)

    open suspend fun SequenceScope<T>.visit(row: Row): Unit =
        row.cells.forEach { visit(it) }

    open suspend fun SequenceScope<T>.visit(cell: Cell): Unit =
        cell.text.forEach { visit(it) }
}
