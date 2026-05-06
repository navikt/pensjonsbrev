package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.*
import no.nav.pensjon.brev.skribenten.letter.Edit.Letter
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList.Item
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.NewLine
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable

val Letter.literals: List<Literal>
    get() = object : EditLetterVisitor<Literal>(this) {
        override fun visit(content: Literal) = emit(content)
    }.build()

val Letter.variables: List<Variable>
    get() = object : EditLetterVisitor<Variable>(this) {
        override fun visit(content: Variable) = emit(content)
    }.build()

abstract class EditLetterVisitor<T>(private val letter: Letter) {
    private val result = mutableListOf<T>()

    protected fun emit(value: T) { result += value }

    fun build(): List<T> {
        visit(letter)
        return result
    }

    open fun visitIdentifiable(element: Edit.Identifiable) = Unit

    open fun visit(letter: Letter): Unit =
        letter.blocks.forEach { visit(it) }

    open fun visit(block: Block): Unit =
        when (block) {
            is Title1 -> visit(block)
            is Title2 -> visit(block)
            is Title3 -> visit(block)
            is Paragraph -> visit(block)
        }

    open fun visit(block: Title1) {
        visitIdentifiable(block)
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Paragraph) {
        visitIdentifiable(block)
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Title2) {
        visitIdentifiable(block)
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Title3) {
        visitIdentifiable(block)
        block.content.forEach { visit(it) }
    }

    open fun visit(content: ParagraphContent): Unit =
        when (content) {
            is ItemList -> visit(content)
            is Table -> visit(content)
            is Text -> visit(content)
        }

    open fun visit(content: Text): Unit =
        when (content) {
            is Literal -> visit(content)
            is Variable -> visit(content)
            is NewLine -> visit(content)
        }

    open fun visit(content: Literal): Unit = visitIdentifiable(content)
    open fun visit(content: Variable): Unit = visitIdentifiable(content)
    open fun visit(content: NewLine): Unit = visitIdentifiable(content)

    open fun visit(itemList: ItemList) {
        visitIdentifiable(itemList)
        itemList.items.forEach { visit(it) }
    }

    open fun visit(item: Item) {
        visitIdentifiable(item)
        item.content.forEach { visit(it) }
    }

    open fun visit(table: Table) {
        visitIdentifiable(table)
        visit(table.header)
        table.rows.forEach { visit(it) }
    }

    open fun visit(header: Header) {
        visitIdentifiable(header)
        header.colSpec.forEach { visit(it) }
    }

    open fun visit(colSpec: ColumnSpec) {
        visitIdentifiable(colSpec)
        visit(colSpec.headerContent)
    }

    open fun visit(row: Row) {
        visitIdentifiable(row)
        row.cells.forEach { visit(it) }
    }

    open fun visit(cell: Cell) {
        visitIdentifiable(cell)
        cell.text.forEach { visit(it) }
    }
}
