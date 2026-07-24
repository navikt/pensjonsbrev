package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.Listetype
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList

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
