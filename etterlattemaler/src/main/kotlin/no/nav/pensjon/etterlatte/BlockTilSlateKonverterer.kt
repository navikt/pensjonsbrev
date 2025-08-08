package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.Slate
import kotlin.collections.ifEmpty
import kotlin.collections.isNotEmpty
import kotlin.collections.toList

internal object BlockTilSlateKonverterer {
    internal fun konverter(it: LetterMarkup): Slate =
        it.blocks
            .flatMap { block -> tilSlateElement(block) }
            .ifEmpty {
                // Tom placeholder dersom ingen innhold slik at Slate får riktig format
                listOf(
                    Element(
                        type = ElementType.PARAGRAPH,
                        children = listOf(InnerElement(type = ElementType.PARAGRAPH, text = "")),
                    ),
                )
            }.let { Slate(it) }

    private fun tilSlateElement(block: LetterMarkup.Block) =
        when (block.type) {
            LetterMarkup.Block.Type.TITLE1 ->
                listOf(
                    Element(
                        type = ElementType.HEADING_TWO,
                        children = (block as LetterMarkup.Block.Title1).content.map { konverterLiteralOgVariable(it) },
                    ),
                )

            LetterMarkup.Block.Type.TITLE2 ->
                listOf(
                    Element(
                        type = ElementType.HEADING_THREE,
                        children = (block as LetterMarkup.Block.Title2).content.map { konverterLiteralOgVariable(it) },
                    ),
                )

            // Hvis en paragraf fra brevbakeren inneholder lister, vil disse splittes ut og legges inn som en
            // Element-node i stedet for en InnerElement-node siden redigering av dette ikke støttes i slate-editoren.
            // De øvrige InnerElementene vil bli slått sammen og lagt til som egne Element-noder.
            LetterMarkup.Block.Type.PARAGRAPH -> {
                val elements: MutableList<Element> = mutableListOf()
                val innerElements: MutableList<InnerElement> = mutableListOf()

                (block as LetterMarkup.Block.Paragraph).content.map {
                    when (it.type) {
                        LetterMarkup.ParagraphContent.Type.LITERAL,
                        LetterMarkup.ParagraphContent.Type.VARIABLE,
                        LetterMarkup.ParagraphContent.Type.NEW_LINE,
                            ->
                            konverterLiteralOgVariable(it).let { innerElement -> innerElements.add(innerElement) }

                        LetterMarkup.ParagraphContent.Type.ITEM_LIST -> {
                            opprettElementFraInnerELementsOgNullstill(innerElements, elements)

                            Element(
                                    type = ElementType.BULLETED_LIST,
                                    children =
                                        (it as LetterMarkup.ParagraphContent.ItemList)
                                            .items
                                            .map { item -> konverterListItem(item) },
                                ).let { element -> elements.add(element) }
                        }
                        else -> {
                            throw IllegalArgumentException("Ukjent type: ${it.type}")
                        }
                    }
                }

                opprettElementFraInnerELementsOgNullstill(innerElements, elements)
                elements
            }
        }

    private fun opprettElementFraInnerELementsOgNullstill(
        innerElements: MutableList<InnerElement>,
        elements: MutableList<Element>,
    ) {
        if (innerElements.isNotEmpty()) {
            elements.add(
                Element(
                    type = ElementType.PARAGRAPH,
                    children = innerElements.toList(),
                ),
            )
            innerElements.clear()
        }
    }

    private fun konverterLiteralOgVariable(it: LetterMarkup.ParagraphContent): InnerElement =
        InnerElement(
            type = ElementType.PARAGRAPH,
            text = (it as LetterMarkup.ParagraphContent.Text).text,
        )

    private fun konverterListItem(it: LetterMarkup.ParagraphContent.ItemList.Item): InnerElement =
        InnerElement(
            type = ElementType.LIST_ITEM,
            children =
                listOf(
                    InnerElement(
                        type = ElementType.PARAGRAPH,
                        text = it.content.joinToString("") { i -> i.text },
                    ),
                ),
        )
}