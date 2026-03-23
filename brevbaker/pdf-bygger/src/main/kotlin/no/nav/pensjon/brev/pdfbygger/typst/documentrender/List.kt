package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList

/**
 * Render an item list as a Typst bulletlist.
 * Output: bulletlist([item1], [item2], ...)
 */
internal fun TypstAppendable.renderList(list: ItemList) {
    if (list.items.isNotEmpty()) {
        appendCodeFunction("bulletlist") {
            args {
                list.items.forEach { item ->
                    contentArg { renderTextContent(item.content) }
                }
            }
        }
    }
}

