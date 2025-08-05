package no.nav.pensjon.brevbaker.api.model

class LetterMarkupValidator {
    companion object {
        fun validate(letterMarkup: LetterMarkup) = validate(letterMarkup.blocks)

        private fun validate(blocks: List<LetterMarkup.Block>) {
            val feil = blocks.flatMap { validate(it) }
            if (feil.isNotEmpty()) {
                throw IllegalLetterMarkupException(feil.joinToString(", ") { it.name })
            }
        }

        private fun validate(block: LetterMarkup.Block) = when (block) {
            is LetterMarkup.Block.Paragraph -> validateParagraph(block)
            is LetterMarkup.Block.Title1 -> listOf()
            is LetterMarkup.Block.Title2 -> listOf()
        }

        private fun validateParagraph(block: LetterMarkup.Block.Paragraph): List<LetterMarkupValideringsfeil> {
            val feil = mutableListOf<LetterMarkupValideringsfeil>()
            if (block.content.isEmpty()) {
                feil.add(LetterMarkupValideringsfeil.TOMT_AVSNITT)
            }
            if (block.content.all { it.erTom() }) {
                feil.add(LetterMarkupValideringsfeil.TOMT_AVSNITT)
            }
            if (harToEtterfoelgendeNewLine(block)) {
                feil.add(LetterMarkupValideringsfeil.TO_ETTERFOELGENDE_NEWLINE)
            }
            feil.addAll(block.content.flatMap { validateParagraphContent(it) })
            return feil
        }

        private fun harToEtterfoelgendeNewLine(block: LetterMarkup.Block.Paragraph): Boolean {
            block.content.mapIndexedNotNull{ index, elem -> index.takeIf{ elem is LetterMarkup.ParagraphContent.Text.NewLine } }.forEach {
                val tomme = block.content.subList(it, block.content.lastIndex)
                    .takeWhile { elem -> elem.erTom() }
                if (tomme.filter { tomt -> tomt is LetterMarkup.ParagraphContent.Text.NewLine }.size > 1) {
                    return true
                }
            }
            return false
        }

        private fun validateParagraphContent(content: LetterMarkup.ParagraphContent) =
            when (content) {
                is LetterMarkup.ParagraphContent.Table -> validateTable(content)
                is LetterMarkup.ParagraphContent.Form.MultipleChoice,
                is LetterMarkup.ParagraphContent.Form.Text,
                is LetterMarkup.ParagraphContent.ItemList,
                is LetterMarkup.ParagraphContent.Text.Literal,
                is LetterMarkup.ParagraphContent.Text.NewLine,
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
}

private fun LetterMarkup.ParagraphContent.erTom() =
    when (this) {
        is LetterMarkup.ParagraphContent.Form.MultipleChoice -> prompt.isEmpty()
        is LetterMarkup.ParagraphContent.Form.Text -> prompt.isEmpty()
        is LetterMarkup.ParagraphContent.ItemList -> items.isEmpty()
        is LetterMarkup.ParagraphContent.Table -> rows.isEmpty() && header.colSpec.isEmpty()
        is LetterMarkup.ParagraphContent.Text.Literal -> text.isBlank()
        is LetterMarkup.ParagraphContent.Text.NewLine -> true
        is LetterMarkup.ParagraphContent.Text.Variable -> false
    }


enum class LetterMarkupValideringsfeil {
    TOMT_AVSNITT,
    FORSKJELLIG_ANTALL_KOLONNER_I_RADER,
    FORSKJELLIG_ANTALL_KOLONNER_HEADER_RADER,
    TO_ETTERFOELGENDE_NEWLINE,
}