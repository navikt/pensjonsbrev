package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport

@LetterTemplateMarker
open class TemplateTableScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element.Table.TableRow<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {

    fun row(colour: Element.Table.RowColour = Element.Table.RowColour.WHITE, init: TemplateTableRowScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Table.TableRow(TemplateTableRowScope<Lang, LetterData>().apply(init).children, colour))
    }

//    fun showIf(
//        predicate: Expression<Boolean>,
//        showIf: TemplateTableRowScope<Lang, LetterData>.() -> Unit
//    ): ShowElseBuilder<Lang, LetterData> {
//        val showElse = mutableListOf<Element.Table.TableRow<Lang>>()
//
//        return TemplateTableRowScope<Lang, LetterData>().apply { showIf() }
//            .let { Element.Conditional(predicate, it.children, showElse) }
//            .also { children.add(it) }
//            .let { ShowElseBuilder(showElse) }
//    }


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
//
//class TableShowElseBuilder<Lang : LanguageSupport, ParameterType : Any>(
//    val showElse: MutableList<Element.Table.TableRow<Lang>> = mutableListOf()
//) {
//    infix fun orShow(init: TemplateTableRowScope<Lang, ParameterType>.() -> Unit): Unit =
//        with(TemplateTableRowScope<Lang, ParameterType>().apply(init)) {
//            showElse.addAll(children)
//        }
//}