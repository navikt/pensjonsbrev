package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.dsl.ExtendedContentBuilder
import no.nav.brev.brevbaker.template.render.RenderContext
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent

/**
 * Skriver ett [ParagraphContent.Text]-element inn i den utvidede markup-DSL-en. Dette er det eneste
 * stedet som både regner ut tekstbitene ([textFragmentsOf]) og legger dem inn i byggeren; selve
 * struktur-traverseringen (kontrollstrukturer, id-er) håndteres av renderen.
 */
internal fun ExtendedContentBuilder.appendText(context: RenderContext, element: ParagraphContent.Text<*>) {
    textFragmentsOf(context, element).forEach { fragment ->
        when (fragment) {
            is TextFragment.Literal -> text(fragment.id, fragment.text, fragment.fontType, fragment.tags)
            is TextFragment.Variable -> variable(fragment.id, fragment.text, fragment.fontType)
            is TextFragment.NewLine -> newLine(fragment.id)
        }
    }
}
