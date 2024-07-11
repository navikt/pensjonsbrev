package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent

abstract class EditedLetterVisitor {
    open fun visit(letter: Edit.Letter) {
        letter.blocks.forEach { visit(it) }
    }

    open fun visit(block: Block) {
        when (block) {
            is Block.Title1 -> visit(block)
            is Block.Title2 -> visit(block)
            is Block.Paragraph -> visit(block)
        }
    }

    open fun visit(block: Block.Title1) {
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Block.Paragraph) {
        block.content.forEach { visit(it) }
    }

    open fun visit(block: Block.Title2) {
        block.content.forEach { visit(it) }
    }

    open fun visit(content: ParagraphContent) {
        when (content) {
            is ParagraphContent.ItemList -> visit(content)
            is ParagraphContent.Table -> visit(content)
            is ParagraphContent.Text -> visit(content)
        }
    }

    open fun visit(content: ParagraphContent.Text) {
        when (content) {
            is ParagraphContent.Text.Literal -> visit(content)
            is ParagraphContent.Text.Variable -> visit(content)
        }
    }
    open fun visit(content: ParagraphContent.Text.Literal) { }
    open fun visit(content: ParagraphContent.Text.Variable) { }

    open fun visit(itemList: ParagraphContent.ItemList) {
        itemList.items.forEach { visit(it) }
    }
    open fun visit(item: ParagraphContent.ItemList.Item) {
        item.content.forEach { visit(it) }
    }

    open fun visit(table: ParagraphContent.Table) {
        visit(table.header)
        table.rows.forEach { visit(it) }
    }
    open fun visit(header: ParagraphContent.Table.Header) {
        header.colSpec.forEach { visit(it) }
    }
    open fun visit(colSpec: ParagraphContent.Table.ColumnSpec) {
        visit(colSpec.headerContent)
    }
    open fun visit(row: ParagraphContent.Table.Row) {
        row.cells.forEach { visit(it) }
    }
    open fun visit(cell: ParagraphContent.Table.Cell) {
        cell.text.forEach { visit(it) }
    }
}