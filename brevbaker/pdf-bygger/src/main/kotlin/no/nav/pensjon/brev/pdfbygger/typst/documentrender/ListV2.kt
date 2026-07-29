package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.brev.brevbaker.markup.outline.Block

internal fun TypstCodeScope.renderItemListV2(list: Block.ItemList) =
    renderListV2("bulletlist", list.items)

internal fun TypstCodeScope.renderNumberedListV2(list: Block.NumberedList) =
    renderListV2("numberedlist", list.items)

private fun TypstCodeScope.renderListV2(functionName: String, items: List<Block.Item>) {
    if (items.isNotEmpty()) {
        appendCodeFunction(functionName) {
            args {
                items.forEach { item ->
                    contentArg { renderTextContentV2(item.content) }
                }
            }
        }
    }
}
