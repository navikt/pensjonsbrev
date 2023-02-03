package no.nav.pensjon.brev.template.render

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import no.nav.pensjon.brev.template.*
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
    private val navLogoImg = "data:image/png;base64,${Base64.getEncoder().encodeToString(getResource("html/nav-logo.png"))}"
    private val fontBinary = listOf(fontFaceCss("normal", 400), fontFaceCss("italic", 400), fontFaceCss("normal", 700))

    private fun fontFaceCss(style: String, weight: Int) =
        """
        @font-face {
            font-family: 'Source Sans Pro';
            font-style: $style;
            font-weight: $weight;
            src: url(data:font/woff2;charset=utf-8;base64,${Base64.getEncoder().encodeToString(getResource("html/SourceSansPro-latin-$style-$weight.woff2"))}) format('woff2');
            unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
        }
        """.trimIndent()

    override fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedHtmlLetter = RenderedHtmlLetter().apply {
        newFile("index.html") {
            appendLine("<!DOCTYPE html>").appendHTML().html {
                lang = scope.language.locale().toLanguageTag()
                head {
                    meta(charset = Charsets.UTF_8.name())
                    meta(name = "viewport", content = "width=device-width")
                    title { renderTextWithoutStyle(scope, template.title) }
                    style { unsafe { fontBinary.forEach { raw(it) } } }
                    style { unsafe { raw(css) } }
                }
                body {
                    div(classes("rot")) {
                        div(classes("brev")) {
                        img(classes = classes("logo"), src = navLogoImg, alt = AltTexts.logo.text(scope.language))
                            div(classes("brevhode")) {
                                renderSakspart(scope)
                                brevdato(scope)
                            }
                            h1(classes("tittel")) { renderText(scope, template.title) }
                            div(classes("brevkropp")) {
                                renderOutline(scope, template.outline)
                                renderClosing(scope)
                            }
                        }
                        render(scope, template.attachments) { attachmentScope, _, attachment ->
                            hr(classes("vedlegg"))
                            renderAttachment(attachmentScope, attachment)
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.brevdato(scope: ExpressionScope<*, *>): Unit =
        div(classes("brevdato")) {
            text(scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)))
        }

    private fun FlowContent.renderClosing(scope: ExpressionScope<*, *>) {
        div("closing") {
            // Har du spørsmål?
            renderOutlineContent(scope, Element.OutlineContent.Title1(languageSettings.settings[LanguageSetting.Closing.harDuSpoersmaal]!!))
            renderParagraph(scope, Element.OutlineContent.Paragraph(languageSettings.settings[LanguageSetting.Closing.kontaktOss]!!))

            // Med vennlig hilsen
            div(classes("closing-greeting")) { renderText(scope, languageSettings.settings[LanguageSetting.Closing.greeting]!!) }
            div(classes("closing-enhet")) { text(scope.felles.avsenderEnhet.navn) }

            val signerende = scope.felles.signerendeSaksbehandlere
            if (signerende != null) {
                div(classes("closing-manuell")) {
                    val saksbehandlerTekst = languageSettings.settings[LanguageSetting.Closing.saksbehandler]!!
                    div(classes("closing-saksbehandler")) {
                        div { text(signerende.saksbehandler) }
                        div { renderText(scope, saksbehandlerTekst) }
                    }
                    div(classes("closing-saksbehandler")) {
                        div { text(signerende.attesterendeSaksbehandler) }
                        div { renderText(scope, saksbehandlerTekst) }
                    }
                }
            } else {
                div(classes("closing-automatisk")) {
                    renderText(scope, languageSettings.settings[LanguageSetting.Closing.automatisk]!!)
                }
            }
        }
    }

    private fun FlowContent.renderAttachment(scope: ExpressionScope<*, *>, template: AttachmentTemplate<*, *>): Unit =
        div(classes("vedlegg")) {
            img(classes = classes("logo"), src = navLogoImg, alt = AltTexts.logo.text(scope.language))
            h1(classes("tittel")) { renderText(scope, listOf(template.title)) }
            if (template.includeSakspart) {
                renderSakspart(scope)
            }
            div(classes("brevkropp")) {
                renderOutline(scope, template.outline)
            }
        }

    private fun FlowContent.renderOutline(scope: ExpressionScope<*, *>, outline: List<OutlineElement<*>>): Unit =
        render(scope, outline) { outlineScope, element ->
            renderOutlineContent(outlineScope, element)
        }

    private fun FlowContent.renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>): Unit =
        when (element) {
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

    private fun FlowOrPhrasingContent.renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>) {
        render(scope, elements) { inner, text -> renderTextContent(inner, text) }
    }

    private fun FlowOrPhrasingContent.renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>) {
        when (element.fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> renderTextContentWithoutStyle(scope, element)
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> span(classes("text-bold")) { renderTextContentWithoutStyle(scope, element) }
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> span(classes("text-italic")) { renderTextContentWithoutStyle(scope, element) }
        }
    }

    private fun Tag.renderTextWithoutStyle(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>) {
        render(scope, elements) { inner, text -> renderTextContentWithoutStyle(inner, text) }
    }

    private fun Tag.renderTextContentWithoutStyle(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> text(element.expr(scope.language).eval(scope))
            is Element.OutlineContent.ParagraphContent.Text.Expression -> text(element.expression.eval(scope))
            is Element.OutlineContent.ParagraphContent.Text.Literal -> text(element.text(scope.language))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> br
        }

    private fun FlowContent.renderForm(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Form<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> {
                div(classes("form-choice")) {
                    div { renderText(scope, listOf(element.prompt)) }
                    element.choices.forEach {
                        div {
                            div(classes("form-choice-checkbox"))
                            div { renderTextContent(scope, it) }
                        }
                    }
                }
            }

            is Element.OutlineContent.ParagraphContent.Form.Text -> {
                div(classes("form-text")) {
                    div { renderText(scope, listOf(element.prompt)) }
                    val size = when (element.size) {
                        Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> "none"
                        Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> "short"
                        Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> "long"
                    }
                    div(classes("form-line-$size")) { }
                }
            }
        }

    private fun FlowContent.renderList(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.ItemList<*>) {
        ul {
            render(scope, element.items) { listScope, content ->
                li { renderText(listScope, content.text) }
            }
        }
    }

    private fun FlowContent.renderTable(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Table<*>) {
        // Small screen
        ul(classes("table")) {
            render(scope, element.rows) { rowScope, row ->
                li {
                    div {
                        row.cells.forEachIndexed { index, cell ->
                            val spec = element.header.colSpec[index]
                            val textClasses = if (index == 0) classes("text-bold") else null

                            if (spec.headerContent.text.isEmpty()) {
                                div { span(textClasses) { renderTextWithoutStyle(scope, cell.text) } }
                                div { }
                            } else {
                                div {
                                    span(textClasses) {
                                        renderTextWithoutStyle(scope, spec.headerContent.text)
                                        text(":")
                                    }
                                }
                                div { span(textClasses) { renderTextWithoutStyle(rowScope, cell.text) } }
                            }
                        }
                    }
                }
            }
        }
        // Big screen
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
        div(classes("sakspart")) {
            with(scope.felles.bruker) {
                val navnPrefix = if (scope.felles.vergeNavn != null) LanguageSetting.Sakspart.gjelderNavn else LanguageSetting.Sakspart.navn

                listOfNotNull(
                    scope.felles.vergeNavn?.let { LanguageSetting.Sakspart.vergenavn to it },
                    navnPrefix to "$fornavn $mellomnavn $etternavn",
                    LanguageSetting.Sakspart.foedselsnummer to foedselsnummer.value,
                    LanguageSetting.Sakspart.saksnummer to scope.felles.saksnummer,
                )
            }.forEach {
                div(classes("sakspart-tittel")) { renderText(scope, languageSettings.settings[it.first]!!) }
                div(classes("sakspart-verdi")) { text(it.second) }
            }
        }
}