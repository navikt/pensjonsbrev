package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import no.nav.brev.brevbaker.template.render.RenderContext
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.UnaryOperation
import java.util.Objects

/**
 * Regner ut tekstbitene ([TextFragment]) for ett enkelt [ParagraphContent.Text]-element, inkludert
 * sammenslåing av nabo-literaler. Dette er ren logikk uten avhengighet til DSL-byggeren –
 * skrivingen til DSL-en skjer i `appendText` (se TextEmitter).
 */
internal fun textFragmentsOf(context: RenderContext, element: ParagraphContent.Text<*>): List<TextFragment> {
    val fontType = element.fontType.toMarkupFontType()
    return when (element) {
        is ParagraphContent.Text.Expression.ByLanguage -> element.expr(context.scope.language).toFragments(context, fontType)
        is ParagraphContent.Text.Expression -> element.expression.toFragments(context, fontType)
        is ParagraphContent.Text.Literal ->
            listOf(TextFragment.Literal(context.stableHash(element), element.text(context.scope.language), fontType, emptySet()))
        is ParagraphContent.Text.NewLine -> listOf(TextFragment.NewLine(context.stableHash(element)))
    }
}

@OptIn(BrevbakerDSLInternal::class)
private fun StringExpression.toFragments(context: RenderContext, fontType: FontType): List<TextFragment> =
    when (this) {
        is Expression.Literal -> literalFragment(context, fontType)
        is Expression.BinaryInvoke<*, *, *> if operation is BinaryOperation.Concat -> {
            // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).toFragments(context, fontType) + (second as StringExpression).toFragments(context, fontType)
        }
        is Expression.BinaryInvoke<*, *, *> if operation is BinaryOperation.BrevdataEllerFritekst -> {
            val (erFritekst, text) = (operation as BinaryOperation.BrevdataEllerFritekst).getResultat(first, second, context.scope)
            if (erFritekst) {
                literalFragment(context, fontType, text, ElementTags.FRITEKST)
            } else {
                variableFragment(context, fontType, text)
            }
        }
        is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.Fritekst -> literalFragment(context, fontType, eval(context.scope), ElementTags.FRITEKST)
        is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.RedigerbarData -> literalFragment(context, fontType, eval(context.scope), ElementTags.REDIGERBAR_DATA)
        else -> variableFragment(context, fontType)
    }.mergeAdjacentLiterals(fontType)

private fun Expression<String>.literalFragment(context: RenderContext, fontType: FontType, text: String = eval(context.scope), tag: ElementTags? = null): List<TextFragment> =
    listOf(TextFragment.Literal(context.stableHash(this), text, fontType, tag?.let { setOf(it) } ?: emptySet()))

private fun Expression<String>.variableFragment(context: RenderContext, fontType: FontType, text: String = eval(context.scope)): List<TextFragment> =
    listOf(TextFragment.Variable(context.stableHash(this), text, fontType))

/**
 * Slår sammen påfølgende literaler til én. Tagget tekst (fritekst/redigerbar data) slås aldri
 * sammen, siden taggene skal bevares som egne biter.
 */
private fun List<TextFragment>.mergeAdjacentLiterals(fontType: FontType): List<TextFragment> =
    fold(emptyList()) { acc, current ->
        val previous = acc.lastOrNull()
        if (acc.isEmpty()) {
            listOf(current)
        } else if (previous is TextFragment.Literal && current is TextFragment.Literal && previous.tags.isEmpty() && current.tags.isEmpty()) {
            acc.subList(0, acc.size - 1) + TextFragment.Literal(Objects.hash(previous.id, current.id), previous.text + current.text, fontType, emptySet())
        } else {
            acc + current
        }
    }

private fun ParagraphContent.Text.FontType.toMarkupFontType(): FontType =
    when (this) {
        ParagraphContent.Text.FontType.PLAIN -> FontType.PLAIN
        ParagraphContent.Text.FontType.BOLD -> FontType.BOLD
        ParagraphContent.Text.FontType.ITALIC -> FontType.ITALIC
    }
