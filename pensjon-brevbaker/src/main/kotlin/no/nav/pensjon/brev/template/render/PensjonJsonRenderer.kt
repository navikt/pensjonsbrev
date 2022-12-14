package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.Block.Type.*
import java.util.*

typealias TreeLocation = List<String>

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(Content.Literal::class, name = "literal"),
    JsonSubTypes.Type(Content.Variable::class, name = "variable"),
)
sealed class Content {
    abstract val id: Int
    abstract val text: String
    abstract val location: TreeLocation

    data class Literal(override val id: Int, override val location: TreeLocation, override val text: String) : Content()
    data class Variable(override val id: Int, override val location: TreeLocation, override val text: String) : Content()
}

data class Block(val id: Int, val type: Type, val location: TreeLocation, val content: List<Content>) {
    enum class Type {
        TITLE1, PARAGRAPH, TEXT
    }
}

data class RenderedJsonLetter(val title: String, val blocks: List<Block>)

object PensjonJsonRenderer {
    fun render(letter: Letter<*>): RenderedJsonLetter =
        letter.toScope().let { scope ->
            RenderedJsonLetter(
                title = renderText(scope, letter.template.title, emptyList()).joinToString { it.text },
                blocks = renderOutline(scope, letter.template.outline)
            )
        }

    private fun <C : Element<*>> render(scope: ExpressionScope<*, *>, elements: List<ContentOrControlStructure<*, C>>, location: TreeLocation, renderBlock: (scope: ExpressionScope<*, *>, element: C, location: TreeLocation) -> Unit) {
        elements.forEach { controlStructure(scope, it, location, renderBlock) }
    }

    private fun <C : Element<*>> controlStructure(scope: ExpressionScope<*, *>, element: ContentOrControlStructure<*, C>, location: TreeLocation, block: (s: ExpressionScope<*, *>, e: C, location: TreeLocation) -> Unit) {
        when (element) {
            is ContentOrControlStructure.Content -> block(scope, element.content, location)

            is ContentOrControlStructure.Conditional -> {
                val body = if (element.predicate.eval(scope)) element.showIf else element.showElse
                render(scope, body, location + "c", block)
            }

            is ContentOrControlStructure.ForEach<*, C, *> -> element.render(scope) { s, e -> controlStructure(s, e, location + "c", block) }
        }
    }

    private fun renderOutline(scope: ExpressionScope<*, *>, outline: List<OutlineElement<*>>): List<Block> =
        mutableListOf<Block>().apply {
            var siblingCounter = 0
            render(scope, outline, emptyList()) { outlineScope, element, location ->
                add(renderOutlineContent(outlineScope, element, location + (siblingCounter++).toString()))
            }
        }

    private fun renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>, location: TreeLocation): Block =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element, location)
            is Element.OutlineContent.Title1 -> Block(element.hashCode(), TITLE1, location, renderText(scope, element.text, location))
            is Element.OutlineContent.ParagraphContent.Form -> TODO()
            is Element.OutlineContent.ParagraphContent.ItemList -> TODO()
            is Element.OutlineContent.ParagraphContent.Table -> TODO()
            is Element.OutlineContent.ParagraphContent.Text -> Block(element.hashCode(), TEXT, location, renderTextContent(scope, element, listOf("0")))
        }

    private fun renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>, currentLocation: TreeLocation): Block =
        Block(paragraph.hashCode(), PARAGRAPH, currentLocation, mutableListOf<Content>().apply {
            var siblingCounter = 0
            render(scope, paragraph.paragraph, currentLocation) { pScope, element, _ ->
                addAll(renderParagraphContent(pScope, element, listOf((siblingCounter++).toString())))
            }
        })

    private fun renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>, location: TreeLocation): List<Content> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element, location)
            is Element.OutlineContent.ParagraphContent.Form -> TODO()
            is Element.OutlineContent.ParagraphContent.ItemList -> TODO()
            is Element.OutlineContent.ParagraphContent.Table -> TODO()
        }

    private fun renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>, location: TreeLocation): List<Content> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(scope.language).toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(Content.Literal(element.hashCode(), location, element.text(scope.language)))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> TODO()
        }

    private fun renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>, currentLocation: TreeLocation): List<Content> =
        mutableListOf<Content>().apply {
            var childLocation = 0
            render(scope, elements, currentLocation) { inner, text, location ->
                addAll(renderTextContent(inner, text, location + (childLocation++).toString()))
            }
        }

    private fun StringExpression.toContent(scope: ExpressionScope<*, *>): List<Content> =
        if (this is Expression.Literal) {
            // TODO: figure out how to handle location
            listOf(Content.Literal(hashCode(), emptyList(), eval(scope)))
        } else if (this is Expression.BinaryInvoke<*, *, *> && operation is BinaryOperation.Concat) {
            // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).toContent(scope) + (second as StringExpression).toContent(scope)
        } else {
            // TODO: figure out how to handle location
            listOf(Content.Variable(hashCode(), emptyList(), eval(scope)))
        }.mergeLiterals()

    private fun List<Content>.mergeLiterals(): List<Content> =
        fold(emptyList()) { acc, content ->
            val last = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(content)
            } else if (last is Content.Literal && content is Content.Literal) {
                acc.subList(0, acc.size - 1) + Content.Literal(Objects.hash(last.id, content.id), last.location, last.text + content.text)
            } else {
                acc + content
            }
        }
}