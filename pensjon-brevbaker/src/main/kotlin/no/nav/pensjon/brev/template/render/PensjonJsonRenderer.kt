package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.template.*
import java.time.format.FormatStyle
import java.util.*

object PensjonJsonRenderer {
    private val languageSettings = pensjonHTMLSettings

    fun render(letter: Letter<*>): RenderedJsonLetter =
        letter.toScope().let { scope ->
            RenderedJsonLetter(
                title = renderText(scope, letter.template.title, emptyList()).joinToString { it.text },
                sakspart = Sakspart(
                    gjelderNavn = gjelderNavn(scope.felles.bruker),
                    gjelderFoedselsnummer = scope.felles.bruker.foedselsnummer.value,
                    saksnummer = scope.felles.saksnummer,
                    dokumentDato = scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)),
                ),
                blocks = renderOutline(scope, letter.template.outline),
                // TODO: Signerende saksbehandler skal være obligatorisk for redigerbare brev, og attesterende skal være nullable
                signatur = scope.felles.signerendeSaksbehandlere.let { sign ->
                    Signatur(
                        hilsenTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.greeting]!!, emptyList()).joinToString { it.text },
                        saksbehandlerRolleTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.saksbehandler]!!, emptyList()).joinToString { it.text },
                        saksbehandlerNavn = sign?.saksbehandler ?: "",
                        attesterendeSaksbehandlerNavn = sign?.attesterendeSaksbehandler ?: "",
                        navAvsenderEnhet = scope.felles.avsenderEnhet.navn,
                    )
                }
            )
        }

    private fun gjelderNavn(bruker: Bruker) =
        listOfNotNull(bruker.fornavn, bruker.mellomnavn, bruker.etternavn).joinToString(" ")

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
            // Har du spørsmål?
            add(
                renderOutlineContent(
                    scope,
                    Element.OutlineContent.Title1(languageSettings.settings[LanguageSetting.Closing.harDuSpoersmaal]!!),
                    listOf((siblingCounter++).toString())
                ).copy(editable = false)
            )
            add(
                renderParagraph(
                    scope,
                    Element.OutlineContent.Paragraph(languageSettings.settings[LanguageSetting.Closing.kontaktOss]!!),
                    listOf((siblingCounter++).toString())
                ).copy(editable = false)
            )
        }

    private fun renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>, location: TreeLocation): Block =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element, location)
            is Element.OutlineContent.Title1 -> Block(element.hashCode(), Block.Type.TITLE1, location, renderText(scope, element.text, location))
            is Element.OutlineContent.ParagraphContent.Form -> TODO()
            is Element.OutlineContent.ParagraphContent.ItemList -> TODO()
            is Element.OutlineContent.ParagraphContent.Table -> TODO()
            is Element.OutlineContent.ParagraphContent.Text -> Block(element.hashCode(), Block.Type.TEXT, location, renderTextContent(scope, element, listOf("0")))
        }

    private fun renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>, currentLocation: TreeLocation): Block =
        Block(paragraph.hashCode(), Block.Type.PARAGRAPH, currentLocation, mutableListOf<Content>().apply {
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
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(Content(Content.Type.LITERAL, element.hashCode(), location, element.text(scope.language)))
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
            listOf(Content(Content.Type.LITERAL, hashCode(), emptyList(), eval(scope)))
        } else if (this is Expression.BinaryInvoke<*, *, *> && operation is BinaryOperation.Concat) {
            // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).toContent(scope) + (second as StringExpression).toContent(scope)
        } else {
            // TODO: figure out how to handle location
            listOf(Content(Content.Type.VARIABLE, hashCode(), emptyList(), eval(scope)))
        }.mergeLiterals()

    private fun List<Content>.mergeLiterals(): List<Content> =
        fold(emptyList()) { acc, content ->
            val last = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(content)
            } else if (last?.type == Content.Type.LITERAL && content.type == Content.Type.LITERAL) {
                acc.subList(0, acc.size - 1) + Content(Content.Type.LITERAL, Objects.hash(last.id, content.id), last.location, last.text + content.text)
            } else {
                acc + content
            }
        }
}