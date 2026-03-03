package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.NumberedList

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

internal fun LatexAppendable.renderList(list: NumberedList) {
    if (list.items.isNotEmpty()) {
        appendCmd("begin", "enumerate")
        list.items.forEach { item ->
            append("""\item """, escape = false)
            renderTextContent(item.content)
        }
        appendCmd("end", "enumerate")
    }
}