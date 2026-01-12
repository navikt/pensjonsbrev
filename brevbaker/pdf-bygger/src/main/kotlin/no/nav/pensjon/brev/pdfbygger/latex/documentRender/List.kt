package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList

internal fun LatexAppendable.renderList(list: ItemList) {
    if (list.items.isNotEmpty()) {
        appendCmd("begin", "letteritemize")
        list.items.forEach { item ->
            append("""\item """, escape = false)
            renderTextContent(item.content)
        }
        appendCmd("end", "letteritemize")
    }
}
