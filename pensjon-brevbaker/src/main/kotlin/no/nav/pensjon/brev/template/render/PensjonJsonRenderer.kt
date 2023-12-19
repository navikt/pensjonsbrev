package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.*
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.Text
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.Text.*
import java.time.format.FormatStyle
import java.util.*

class PensjonJsonRendererException(msg: String) : Exception(msg)

object PensjonJsonRenderer : LetterRenderer<RenderedJsonLetter>() {
    private val languageSettings = pensjonHTMLSettings

    override fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedJsonLetter =
        RenderedJsonLetter(
            title = renderText(scope, template.title).joinToString { it.text },
            sakspart = Sakspart(
                gjelderNavn = scope.felles.bruker.fulltNavn(),
                gjelderFoedselsnummer = scope.felles.bruker.foedselsnummer.value,
                saksnummer = scope.felles.saksnummer,
                dokumentDato = scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)),
            ),
            blocks = renderOutline(scope, template.outline),
            // TODO: Attesterende saksbehandler må kunne være null for informasjonsskriv som ikke attesteres
            signatur = scope.felles.signerendeSaksbehandlere.let { sign ->
                Signatur(
                    hilsenTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.greeting]!!).joinToString { it.text },
                    saksbehandlerRolleTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.saksbehandler]!!).joinToString { it.text },
                    saksbehandlerNavn = sign?.saksbehandler ?: "",
                    attesterendeSaksbehandlerNavn = sign?.attesterendeSaksbehandler ?: "",
                    navAvsenderEnhet = scope.felles.avsenderEnhet.navn,
                )
            }
        )

    private fun renderOutline(scope: ExpressionScope<*, *>, outline: List<OutlineElement<*>>): List<Block> =
        mutableListOf<Block>().apply {
            render(scope, outline) { outlineScope, element ->
                add(renderOutlineContent(outlineScope, element))
            }
        }

    private fun renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>): Block =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> Block.Title1(element.hashCode(), true, renderText(scope, element.text))
            is Element.OutlineContent.Title2 -> Block.Title2(element.hashCode(), true, renderText(scope, element.text))
        }

    private fun renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>): Paragraph =
        Paragraph(paragraph.hashCode(), true, mutableListOf<ParagraphContent>().apply {
            render(scope, paragraph.paragraph) { pScope, element ->
                addAll(renderParagraphContent(pScope, element))
            }
        })

    private fun renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>): List<ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(renderItemList(scope, element))
            is Element.OutlineContent.ParagraphContent.Table -> TODO()
            is Element.OutlineContent.ParagraphContent.Form -> throw PensjonJsonRendererException("Can't render unsupported element: Form.*")
        }

    private fun renderItemList(scope: ExpressionScope<*, *>, itemList: Element.OutlineContent.ParagraphContent.ItemList<*>): ParagraphContent.ItemList =
        ParagraphContent.ItemList(itemList.hashCode(), mutableListOf<ParagraphContent.ItemList.Item>().apply {
            render(scope, itemList.items) { inner, item ->
                add(ParagraphContent.ItemList.Item(renderText(inner, item.text)))
            }
        })

    private fun renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>): List<Text> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(scope.language).toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toContent(scope)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(Literal(element.hashCode(), element.text(scope.language)))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> throw PensjonJsonRendererException("Can't render unsupported element: NewLine")
        }

    private fun renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>): List<Text> =
        mutableListOf<Text>().apply {
            render(scope, elements) { inner, text ->
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