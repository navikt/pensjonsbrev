package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.dsl.ExtendedContentBuilder
import no.nav.brev.brevbaker.markup.dsl.PlainExtendedTextBuilder
import no.nav.brev.brevbaker.template.render.RenderContext
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent

internal fun ExtendedContentBuilder.appendText(context: RenderContext, element: ParagraphContent.Text<*>) {
    textFragmentsOf(context, element).forEach { fragment ->
        when (fragment) {
            is TextFragment.Literal -> text(fragment.id, fragment.text, fragment.fontType, fragment.tags)
            is TextFragment.Variable -> variable(fragment.id, fragment.text, fragment.fontType)
            is TextFragment.NewLine -> newLine(fragment.id)
        }
    }
}

internal fun PlainExtendedTextBuilder.appendText(context: RenderContext, element: ParagraphContent.Text<*>) {
    textFragmentsOf(context, element).forEach { fragment ->
        when (fragment) {
            is TextFragment.Literal -> text(fragment.id, fragment.text)
            is TextFragment.Variable -> variable(fragment.id, fragment.text)
            is TextFragment.NewLine -> Unit
        }
    }
}
