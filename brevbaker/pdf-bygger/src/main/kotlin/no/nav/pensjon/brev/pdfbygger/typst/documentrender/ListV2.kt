package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.ListContent

internal fun TypstCodeScope.renderListV2(list: ListContent) {
    if (list.items.isNotEmpty()) {
        val functionName = when (list) {
            is ListContent.ItemList -> "bulletlist"
            is ListContent.NumberedList -> "numberedlist"
        }
        appendCodeFunction(functionName) {
            args {
                list.items.forEach { item ->
                    contentArg { renderTextContentV2(item.content) }
                }
            }
        }
    }
}
