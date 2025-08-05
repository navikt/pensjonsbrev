package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.NewLine

object LetterMarkupValidator {
    private const val SKAL_VALIDERE_TOMT_AVSNITT = false

    fun validate(letterMarkup: LetterMarkup) = validate(letterMarkup.blocks)

    private fun validate(blocks: List<LetterMarkup.Block>) =
        blocks.flatMap { validate(it) }.takeIf { it.isNotEmpty() }?.let {
            throw IllegalLetterMarkupException(it.joinToString(", ") { it.name })
        }

    private fun validate(block: LetterMarkup.Block) = when (block) {
        is LetterMarkup.Block.Paragraph -> validateParagraph(block)
        is LetterMarkup.Block.Title1 -> listOf()
        is LetterMarkup.Block.Title2 -> listOf()
    }

    private fun validateParagraph(block: LetterMarkup.Block.Paragraph): List<LetterMarkupValideringsfeil> {
        val feil = mutableListOf<LetterMarkupValideringsfeil>()
        if (SKAL_VALIDERE_TOMT_AVSNITT && block.content.isNotEmpty() && block.content.all { it.erTom() }) {
            feil.add(LetterMarkupValideringsfeil.AVSNITT_UTEN_INNHOLD)
        }
        if (harToEtterfoelgendeNewLine(block)) {
            feil.add(LetterMarkupValideringsfeil.TO_ETTERFOELGENDE_NEWLINE)
        }
        feil.addAll(block.content.flatMap { validateParagraphContent(it) })
        return feil
    }

    private fun harToEtterfoelgendeNewLine(block: LetterMarkup.Block.Paragraph) = block.content
        .mapIndexedNotNull { index, elem -> index.takeIf { elem is NewLine } }
        .any {
            block.content.subList(it, block.content.lastIndex + 1)
                .takeWhile { it.erTom() }
                .filter { it is NewLine }.size > 1
        }

    private fun validateParagraphContent(content: LetterMarkup.ParagraphContent) =
        when (content) {
            is LetterMarkup.ParagraphContent.Table -> validateTable(content)
            is LetterMarkup.ParagraphContent.Form.MultipleChoice,
            is LetterMarkup.ParagraphContent.Form.Text,
            is LetterMarkup.ParagraphContent.ItemList,
            is LetterMarkup.ParagraphContent.Text.Literal,
            is NewLine,
            is LetterMarkup.ParagraphContent.Text.Variable,
                -> listOf()
        }

    private fun validateTable(content: LetterMarkup.ParagraphContent.Table): List<LetterMarkupValideringsfeil> {
        val feil = mutableListOf<LetterMarkupValideringsfeil>()
        val antallKolonnerHeader = content.header.colSpec.size
        val antallKolonnerRader = content.rows.map { it.cells.size }.distinct()
        if (antallKolonnerRader.size > 1) {
            feil.add(LetterMarkupValideringsfeil.FORSKJELLIG_ANTALL_KOLONNER_I_RADER)
        }
        if (antallKolonnerHeader != antallKolonnerRader.first()) {
            feil.add(LetterMarkupValideringsfeil.FORSKJELLIG_ANTALL_KOLONNER_HEADER_RADER)
        }
        return feil
    }
}

private fun LetterMarkup.ParagraphContent.erTom() =
    when (this) {
        is LetterMarkup.ParagraphContent.Form.MultipleChoice -> prompt.isEmpty()
        is LetterMarkup.ParagraphContent.Form.Text -> prompt.isEmpty()
        is LetterMarkup.ParagraphContent.ItemList -> false
        is LetterMarkup.ParagraphContent.Table -> rows.isEmpty() && header.colSpec.isEmpty()
        is LetterMarkup.ParagraphContent.Text.Literal -> text.isBlank()
        is NewLine -> true
        is LetterMarkup.ParagraphContent.Text.Variable -> false
    }


enum class LetterMarkupValideringsfeil {
    AVSNITT_UTEN_INNHOLD,
    FORSKJELLIG_ANTALL_KOLONNER_I_RADER,
    FORSKJELLIG_ANTALL_KOLONNER_HEADER_RADER,
    TO_ETTERFOELGENDE_NEWLINE,
}