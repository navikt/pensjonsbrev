package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSupport

@LetterTemplateMarker
open class TemplateTableScope<Lang : LanguageSupport, LetterData : Any>(
    var title: List<Element<Lang>>? = null,
    val children: MutableList<Element.Table.Row<Lang>> = mutableListOf()
) :
    TemplateGlobalScope<LetterData>() {

    fun title(
        init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit
    ) {
        title = TemplateTextOnlyScope<Lang, LetterData>().apply(init).children
    }

    fun row(
        colour: Element.Table.RowColour = Element.Table.RowColour.WHITE,
        init: TemplateTableRowScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.Table.Row(TemplateTableRowScope<Lang, LetterData>().apply(init).children, colour))
    }
}

@LetterTemplateMarker
open class TemplateTableRowScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element.Table.Cell<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {
    fun cell(cellColumns: Int = 1, init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table.Cell(
                TemplateTextOnlyScope<Lang, LetterData>().apply(init).children,
                cellColumns
            )
        )
    }
}