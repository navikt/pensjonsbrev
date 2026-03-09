package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.brev.Listetype
import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList

internal fun LatexAppendable.renderList(list: ItemList) {
    if (list.items.isNotEmpty()) {
        val command = when (list.listType) {
            Listetype.PUNKTLISTE -> "letteritemize"
            Listetype.NUMMERERT_LISTE -> "letterenumerate"
        }
        appendCmd("begin", command)
        list.items.forEach { item ->
            append("""\item """, escape = false)
            renderTextContent(item.content)
        }
        appendCmd("end", command)
    }
}
