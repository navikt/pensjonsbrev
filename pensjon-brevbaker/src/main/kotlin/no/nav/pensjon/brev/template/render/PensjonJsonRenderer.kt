package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.Block.Paragraph
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.ParagraphContent.Text
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.ParagraphContent.Text.*
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
                    gjelderNavn = scope.felles.bruker.fulltNavn(),
                    gjelderFoedselsnummer = scope.felles.bruker.foedselsnummer.value,
                    saksnummer = scope.felles.saksnummer,
                    dokumentDato = scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)),
                ),
                blocks = renderOutline(scope, letter.template.outline),
                // TODO: Attesterende saksbehandler må kunne være null for informasjonsskriv som ikke attesteres
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
                languageSettings.settings[LanguageSetting.Closing.harDuSpoersmaal]!!.let {
                    val loc = listOf((siblingCounter++).toString())
                    Block.Title1(it.hashCode(), loc, false, renderText(scope, it, loc))
                }
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
            is Element.OutlineContent.Title1 -> Block.Title1(element.hashCode(), location, true, renderText(scope, element.text, location))
        }

    private fun renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>, currentLocation: TreeLocation): Paragraph =
        Paragraph(paragraph.hashCode(), currentLocation, true, mutableListOf<ParagraphContent>().apply {
            render(scope, paragraph.paragraph, currentLocation) { pScope, element, _ ->
                addAll(renderParagraphContent(pScope, element))
            }
        })

    private fun renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>): List<ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(renderItemList(scope, element))
            is Element.OutlineContent.ParagraphContent.Table -> TODO()
            is Element.OutlineContent.ParagraphContent.Form -> TODO()
        }

    private fun renderItemList(scope: ExpressionScope<*, *>, itemList: Element.OutlineContent.ParagraphContent.ItemList<*>): ParagraphContent.ItemList =
        ParagraphContent.ItemList(itemList.hashCode(), mutableListOf<ParagraphContent.ItemList.Item>().apply {
            render(scope, itemList.items, emptyList()) { inner, item, _ ->
                add(ParagraphContent.ItemList.Item(renderText(inner, item.text, emptyList())))
            }
        })

    private fun renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>): List<Text> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(scope.language).toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(Literal(element.hashCode(), element.text(scope.language)))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> TODO()
        }

    private fun renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>, currentLocation: TreeLocation): List<Text> =
        mutableListOf<Text>().apply {
            render(scope, elements, currentLocation) { inner, text, _ ->
                addAll(renderTextContent(inner, text))
            }
        }

    private fun StringExpression.toContent(scope: ExpressionScope<*, *>): List<Text> =
        if (this is Expression.Literal) {
            listOf(Literal(hashCode(), eval(scope)))
        } else if (this is Expression.BinaryInvoke<*, *, *> && operation is BinaryOperation.Concat) {
            // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).toContent(scope) + (second as StringExpression).toContent(scope)
        } else {
            listOf(Variable(hashCode(), eval(scope)))
        }.mergeLiterals()

    private fun List<Text>.mergeLiterals(): List<Text> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (previous is Literal && current is Literal) {
                acc.subList(0, acc.size - 1) + Literal(Objects.hash(previous.id, current.id), previous.text + current.text)
            } else {
                acc + current
            }
        }
}