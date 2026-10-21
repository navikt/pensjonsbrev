package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

/**
 * Updates the values of variables already present in the letter with values from a fresh rendering,
 * without merging any structural changes (added/removed blocks or content) from the rendering.
 * Variables not present in the fresh rendering keep their previous value.
 */
fun Edit.Letter.updateVariables(renderedLetter: LetterMarkup): Edit.Letter =
    renderedLetter.toEdit().let { rendered ->
        UpdateVariables(rendered.variablesValueMap() + rendered.title.variablesValueMap()).updateLetter(this)
    }

private fun Edit.Title.variablesValueMap(): Map<Int, String> =
    text.filterIsInstance<Edit.ParagraphContent.Text.Variable>().mapNotNull { v -> v.id?.let { it to v.text } }.toMap()


private class UpdateVariables(private val variableValues: Map<Int, String>) {
    fun updateLetter(letter: Edit.Letter): Edit.Letter =
        letter.copy(
            title = letter.title.copy(text = letter.title.text.map(::update)),
            blocks = letter.blocks.map(::update),
        )

    private fun update(block: Edit.Block): Edit.Block =
        when (block) {
            is Edit.Block.Title1 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Title2 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Title3 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Paragraph -> block.copy(content = block.content.map(::update))
        }

    private fun update(content: Edit.ParagraphContent): Edit.ParagraphContent =
        when (content) {
            is Edit.ParagraphContent.Text -> update(content)
            is Edit.ParagraphContent.ItemList -> content.copy(items = content.items.map { it.copy(content = it.content.map(::update)) })
            is Edit.ParagraphContent.Table -> content.copy(
                header = content.header.copy(colSpec = content.header.colSpec.map { it.copy(headerContent = update(it.headerContent)) }),
                rows = content.rows.map { row -> row.copy(cells = row.cells.map(::update)) },
            )
        }

    private fun update(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        cell.copy(text = cell.text.map(::update))

    private fun update(text: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (text) {
            is Edit.ParagraphContent.Text.Variable -> text.id?.let(variableValues::get)?.let { text.copy(text = it) } ?: text
            is Edit.ParagraphContent.Text.Literal, is Edit.ParagraphContent.Text.NewLine -> text
        }
}