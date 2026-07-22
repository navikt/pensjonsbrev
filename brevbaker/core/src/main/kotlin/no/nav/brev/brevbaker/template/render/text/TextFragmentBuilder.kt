package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.outline.EditBehaviour
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
 * Rendrer til text fragmenter ([TextFragment]) for ett enkelt [ParagraphContent.Text]-element, inkludert
 * sammenslåing av nabo-literaler.
 */
internal fun textFragmentsOf(context: RenderContext, element: ParagraphContent.Text<*>): List<TextFragment> {
    val fontType = element.fontType.toMarkupFontType()
    return when (element) {
        is ParagraphContent.Text.Expression.ByLanguage -> element.expr(context.scope.language).toFragments(context, fontType)
        is ParagraphContent.Text.Expression -> element.expression.toFragments(context, fontType)
        is ParagraphContent.Text.Literal ->
            listOf(TextFragment.Literal(context.stableHash(element), element.text(context.scope.language), fontType, null))
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
                literalFragment(context, fontType, text, EditBehaviour.FRITEKST)
            } else {
                variableFragment(context, fontType, text)
            }
        }
        is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.Fritekst -> literalFragment(context, fontType, eval(context.scope), EditBehaviour.FRITEKST)
        is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.RedigerbarData -> literalFragment(context, fontType, eval(context.scope), EditBehaviour.REDIGERBAR_DATA)
        else -> variableFragment(context, fontType)
    }.mergeAdjacentLiterals(fontType)

private fun Expression<String>.literalFragment(context: RenderContext, fontType: FontType, text: String = eval(context.scope), editBehaviour: EditBehaviour? = null): List<TextFragment> =
    listOf(TextFragment.Literal(context.stableHash(this), text, fontType, editBehaviour))

private fun Expression<String>.variableFragment(context: RenderContext, fontType: FontType, text: String = eval(context.scope)): List<TextFragment> =
    listOf(TextFragment.Variable(context.stableHash(this), text, fontType))

private fun List<TextFragment>.mergeAdjacentLiterals(fontType: FontType): List<TextFragment> =
    fold(emptyList()) { acc, current ->
        val previous = acc.lastOrNull()
        if (acc.isEmpty()) {
            listOf(current)
        } else if (previous is TextFragment.Literal && current is TextFragment.Literal && previous.editBehaviour == null && current.editBehaviour == null) {
            acc.subList(0, acc.size - 1) + TextFragment.Literal(Objects.hash(previous.id, current.id), previous.text + current.text, fontType, null)
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
