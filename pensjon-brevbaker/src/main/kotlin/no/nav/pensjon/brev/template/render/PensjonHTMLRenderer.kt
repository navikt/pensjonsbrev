package no.nav.pensjon.brev.template.render

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.pensjonlatex.*
import java.time.format.FormatStyle
import java.util.*


private object AltTexts {
    val logo = Element.OutlineContent.ParagraphContent.Text.Literal.create(
        Language.Bokmal to "NAV logo",
        Language.Nynorsk to "NAV logo",
        Language.English to "NAV logo",
    )
}

object PensjonHTMLRenderer : LetterRenderer<RenderedHtmlLetter>() {

    private val languageSettings = pensjonHTMLSettings
    private val css = getResource("html/style.css").toString(Charsets.UTF_8)
    private val navLogoImg = "data:image/png;base64, ${Base64.getEncoder().encodeToString(getResource("html/nav-logo.png"))}"

    private fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find resource /$fileName""")

    override fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedHtmlLetter = RenderedHtmlLetter().apply {
        newFile("index.html") {
            appendLine("<!DOCTYPE html>").appendHTML().html {
                lang = scope.language.locale().toLanguageTag()
                head {
                    meta(charset = Charsets.UTF_8.name())
                    title { renderText(scope, template.title) }
                    style { unsafe { raw(css) } }
                }
                body {
                    img(classes = classes("logo"), src = navLogoImg, alt = AltTexts.logo.text(scope.language))
                    div(classes("brev")) {
                        h1(classes("tittel")) { renderText(scope, template.title) }
                        div(classes("brevhode")) {
                            renderSakspart(scope)
                            div(classes("brevdato")) {
                                text(scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)))
                            }
                        }
                        div(classes("brevkropp")) {
                            render(scope, template.outline) { outlineScope, element ->
                                renderOutlineContent(outlineScope, element)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent -> renderParagraphContent(scope, element)
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> h2(classes("title1")) { renderText(scope, element.text) }
        }

    private fun FlowContent.renderParagraph(scope: ExpressionScope<*, *>, paragraph: Element.OutlineContent.Paragraph<*>) {
        div(classes("paragraph")) {
            render(scope, paragraph.paragraph) { pScope, element ->
                renderParagraphContent(pScope, element)
            }
        }
    }

    private fun FlowContent.renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>) {
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> renderForm(scope, element)
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> renderList(scope, element)
            is Element.OutlineContent.ParagraphContent.Table -> renderTable(scope, element)
        }
    }

    private fun Tag.renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>) {
        render(scope, elements) { inner, text -> renderTextContent(inner, text) }
    }

    private fun Tag.renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>) {
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> text(element.expr(scope.language).eval(scope))
            is Element.OutlineContent.ParagraphContent.Text.Expression -> text(element.expression.eval(scope))
            is Element.OutlineContent.ParagraphContent.Text.Literal -> text(element.text(scope.language))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> br
        }
    }

    private fun FlowContent.renderForm(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Form<*>) {
        TODO()
    }

    private fun FlowContent.renderList(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.ItemList<*>) {
        ul {
            render(scope, element.items) { listScope, content ->
                li { renderText(listScope, content.text) }
            }
        }
    }

    private fun FlowContent.renderTable(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Table<*>) {
        table(classes("table")) {
            thead {
                tr {
                    element.header.colSpec.forEach {
                        th(classes = classes(alignmentClass(it.alignment))) {
                            colSpan = it.columnSpan.toString()
                            renderText(scope, it.headerContent.text)
                        }
                    }
                }
            }
            tbody {
                render(scope, element.rows) { rowScope, row ->
                    tr {
                        row.cells.forEachIndexed { index, cell ->
                            val spec = element.header.colSpec[index]
                            td(classes = classes(alignmentClass(spec.alignment))) {
                                colSpan = spec.columnSpan.toString()
                                renderText(rowScope, cell.text)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun classes(vararg classes: String?): String =
        classes.filterNotNull().joinToString(" ") { "pensjonsbrev-$it" }

    private fun alignmentClass(alignment: Element.OutlineContent.ParagraphContent.Table.ColumnAlignment): String =
        when (alignment) {
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> "text-left"
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> "text-right"
        }

    private fun FlowContent.renderSakspart(scope: ExpressionScope<*, *>) =
        table(classes("sakspart")) {
            tbody {
                with(scope.felles.bruker) {
                    listOf(
                        LanguageSetting.Sakspart.navn to "$fornavn $mellomnavn $etternavn",
                        LanguageSetting.Sakspart.foedselsnummer to foedselsnummer.value,
                        LanguageSetting.Sakspart.saksnummer to scope.felles.saksnummer,
                    )
                }.forEach {
                    tr {
                        th { renderText(scope, languageSettings.settings[it.first]!!) }
                        td { text(it.second) }
                    }
                }
            }
        }
}