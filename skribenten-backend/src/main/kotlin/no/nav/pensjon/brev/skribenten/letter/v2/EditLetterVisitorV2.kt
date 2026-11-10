package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Item
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Text

abstract class EditLetterVisitorV2<T>(private val blocks: List<Block>) {
    constructor(letter: EditV2.Letter) : this(letter.blocks)
    constructor(attachment: EditV2.Attachment) : this(attachment.blocks)

    private val result = mutableListOf<T>()

    protected fun emit(value: T) {
        result += value
    }

    fun build(): List<T> {
        blocks.forEach { visit(it) }
        return result
    }

    open fun visitIdentifiable(element: EditV2.Identifiable) = Unit

    open fun visit(block: Block): Unit =
        when (block) {
            is Block.TextBlock -> visit(block)
            is Block.ListBlock -> visit(block)
            is Table -> visit(block)
        }

    open fun visit(block: Block.TextBlock) {
        visitIdentifiable(block)
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Block.ListBlock) {
        visitIdentifiable(block)
        block.items.forEach { visit(it) }
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

    open fun visit(header: Table.Header) {
        visitIdentifiable(header)
        header.colSpec.forEach { visit(it) }
    }

    open fun visit(colSpec: Table.ColumnSpec) {
        visitIdentifiable(colSpec)
        colSpec.content.forEach { visit(it) }
    }

    open fun visit(row: Table.Row) {
        visitIdentifiable(row)
        row.cells.forEach { visit(it) }
    }

    open fun visit(cell: Table.Cell) {
        visitIdentifiable(cell)
        cell.content.forEach { visit(it) }
    }

    open fun visit(text: Text): Unit =
        when (text) {
            is Text.Literal -> visit(text)
            is Text.Variable -> visit(text)
            is Text.NewLine -> visit(text)
        }

    open fun visit(text: Text.Literal): Unit = visitIdentifiable(text)
    open fun visit(text: Text.Variable): Unit = visitIdentifiable(text)
    open fun visit(text: Text.NewLine): Unit = visitIdentifiable(text)
}

fun EditV2.Letter.variablesValueMap(): Map<Int, String> = blocks.variablesValueMap()

fun EditV2.Attachment.variablesValueMap(): Map<Int, String> = blocks.variablesValueMap()

private fun List<Block>.variablesValueMap(): Map<Int, String> =
    object : EditLetterVisitorV2<Text.Variable>(this) {
        override fun visit(text: Text.Variable) = emit(text)
    }.build().mapNotNull { v -> v.id?.let { it to v.text } }.toMap()
