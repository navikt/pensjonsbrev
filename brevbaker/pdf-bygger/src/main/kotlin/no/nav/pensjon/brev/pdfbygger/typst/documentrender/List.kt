package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.Listetype
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList

/**
 * Render an item list as a Typst list.
 * Output: bulletlist([item1], [item2], ...) or numberedlist([item1], [item2], ...)
 */
internal fun TypstCodeScope.renderList(list: ItemList) {
    if (list.items.isNotEmpty()) {
        val functionName = when (list.listType) {
            Listetype.PUNKTLISTE -> "bulletlist"
            Listetype.NUMMERERT_LISTE -> "numberedlist"
        }
        appendCodeFunction(functionName) {
            args {
                list.items.forEach { item ->
                    contentArg { renderTextContent(item.content) }
                }
            }
        }
    }
}
