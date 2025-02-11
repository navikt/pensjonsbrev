package no.nav.pensjon.brev.template.render

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.render.LanguageSetting.Closing.AUTOMATISK_INFORMASJONSBREV
import no.nav.pensjon.brev.template.render.LanguageSetting.Closing.AUTOMATISK_VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import java.time.format.FormatStyle
import java.util.*

object HTMLDocumentRenderer : DocumentRenderer<HTMLDocument> {
    private val languageSettings = pensjonHTMLSettings
    private val css = getResource("html/style.css").toString(Charsets.UTF_8)
    private val navLogoImg =
        "data:image/png;base64,${Base64.getEncoder().encodeToString(getResource("html/nav-logo.png"))}"
    private val fontBinary = listOf(fontFaceCss("normal", 400), fontFaceCss("italic", 400), fontFaceCss("normal", 700))

    private fun fontFaceCss(
        style: String,
        weight: Int,
    ) =
        """
        @font-face {
            font-family: 'Source Sans Pro';
            font-style: $style;
            font-weight: $weight;
            src: url(data:font/woff2;charset=utf-8;base64,${
            Base64.getEncoder().encodeToString(getResource("html/SourceSansPro-latin-$style-$weight.woff2"))
        }) format('woff2');
            unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
        }
        """.trimIndent()

    override fun render(
        letter: LetterMarkup,
        attachments: List<Attachment>,
        language: Language,
        felles: Felles,
        brevtype: LetterMetadata.Brevtype,
    ): HTMLDocument =
        HTMLDocument {
            appendLine("<!DOCTYPE html>").appendHTML().html {
                lang = language.locale().toLanguageTag()
                head {
                    meta(charset = Charsets.UTF_8.name())
                    meta(name = "viewport", content = "width=device-width")
                    title { text(letter.title) }
                    style { unsafe { fontBinary.forEach { raw(it) } } }
                    style { unsafe { raw(css) } }
                }
                body {
                    div(classes("rot")) {
                        div(classes("brev")) {
                            img(
                                classes = classes("logo"),
                                src = navLogoImg,
                                alt = languageSettings.getSetting(language, LanguageSetting.HTML.ALT_TEXT_LOGO),
                            )
                            div(classes("brevhode")) {
                                renderSakspart(language, felles)
                                brevdato(language, felles)
                            }
                            h1(classes("tittel")) { text(letter.title) }
                            div(classes("brevkropp")) {
                                letter.blocks.forEach { renderBlock(it) }
                                renderClosing(language, felles, brevtype)
                            }
                        }
                        attachments.forEach {
                            hr(classes("vedlegg"))
                            renderAttachment(it, language, felles)
                        }
                    }
                }
            }
        }

    private fun FlowContent.brevdato(
        language: Language,
        felles: Felles,
    ): Unit =
        div(classes("brevdato")) {
            text(felles.dokumentDato.format(dateFormatter(language, FormatStyle.SHORT)))
        }

    private fun FlowContent.renderClosing(
        language: Language,
        felles: Felles,
        brevtype: LetterMetadata.Brevtype,
    ) {
        div("closing") {
            // Med vennlig hilsen
            div(classes("closing-greeting")) {
                text(languageSettings.getSetting(language, LanguageSetting.Closing.GREETING))
            }
            div(classes("closing-enhet")) { text(felles.avsenderEnhet.navn) }

            val signerende = felles.signerendeSaksbehandlere
            if (signerende != null) {
                div(classes("closing-manuell")) {
                    val saksbehandlerTekst = languageSettings.getSetting(language, LanguageSetting.Closing.SAKSBEHANDLER)
                    signerende.attesterendeSaksbehandler?.takeIf { brevtype == VEDTAKSBREV }?.let {
                        div(classes("closing-saksbehandler")) {
                            div { text(it) }
                            div { text(saksbehandlerTekst) }
                        }
                    }
                    div(classes("closing-saksbehandler")) {
                        div { text(signerende.saksbehandler) }
                        div { text(saksbehandlerTekst) }
                    }
                }
            } else {
                div(classes("closing-automatisk")) {
                    if (brevtype == VEDTAKSBREV) {
                        text(languageSettings.getSetting(language, AUTOMATISK_VEDTAKSBREV))
                    } else {
                        text(languageSettings.getSetting(language, AUTOMATISK_INFORMASJONSBREV))
                    }
                }
            }
        }
    }

    private fun FlowContent.renderAttachment(
        attachment: Attachment,
        language: Language,
        felles: Felles,
    ): Unit =
        div(classes("vedlegg")) {
            img(classes = classes("logo"), src = navLogoImg, alt = languageSettings.getSetting(language, LanguageSetting.HTML.ALT_TEXT_LOGO))
            h1(classes("tittel")) { renderText(attachment.title) }
            if (attachment.includeSakspart) {
                renderSakspart(language, felles)
            }
            div(classes("brevkropp")) {
                attachment.blocks.forEach { renderBlock(it) }
            }
        }

    private fun FlowContent.renderBlock(block: Block): Unit =
        when (block) {
            is Block.Paragraph -> renderParagraph(block)
            is Block.Title1 -> h2(classes("title1")) { renderText(block.content) }
            is Block.Title2 -> h3(classes("title2")) { renderText(block.content) }
        }

    private fun FlowContent.renderParagraph(paragraph: Block.Paragraph) {
        div(classes("paragraph")) {
            paragraph.content.forEach { renderParagraphContent(it) }
        }
    }

    private fun FlowContent.renderParagraphContent(element: ParagraphContent) {
        when (element) {
            is ParagraphContent.Form -> renderForm(element)
            is ParagraphContent.Text -> renderTextContent(element)
            is ParagraphContent.ItemList -> renderList(element)
            is ParagraphContent.Table -> renderTable(element)
        }
    }

    private fun FlowOrPhrasingContent.renderText(elements: List<ParagraphContent.Text>) {
        elements.forEach { renderTextContent(it) }
    }

    private fun FlowOrPhrasingContent.renderTextContent(element: ParagraphContent.Text) {
        when (element.fontType) {
            FontType.PLAIN -> renderTextContentWithoutStyle(element)
            FontType.BOLD ->
                span(classes("text-bold")) {
                    renderTextContentWithoutStyle(element)
                }
            FontType.ITALIC ->
                span(classes("text-italic")) {
                    renderTextContentWithoutStyle(element)
                }
        }
    }

    private fun Tag.renderTextWithoutStyle(elements: List<ParagraphContent.Text>) {
        elements.forEach { renderTextContentWithoutStyle(it) }
    }

    private fun Tag.renderTextContentWithoutStyle(element: ParagraphContent.Text) {
        when (element) {
            is ParagraphContent.Text.Variable -> text(element.text)
            is ParagraphContent.Text.Literal -> text(element.text)
            is ParagraphContent.Text.NewLine -> br
        }
    }

    private fun FlowContent.renderForm(element: ParagraphContent.Form) {
        when (element) {
            is ParagraphContent.Form.MultipleChoice -> {
                div(classes("form-choice")) {
                    div { renderText(element.prompt) }
                    element.choices.forEach {
                        div {
                            div(classes("form-choice-checkbox"))
                            div { renderText(it.text) }
                        }
                    }
                }
            }

            is ParagraphContent.Form.Text -> {
                div(classes("form-text")) {
                    div { renderText(element.prompt) }
                    val size =
                        when (element.size) {
                            ParagraphContent.Form.Text.Size.NONE -> "none"
                            ParagraphContent.Form.Text.Size.SHORT -> "short"
                            ParagraphContent.Form.Text.Size.LONG -> "long"
                        }
                    div(classes("form-line-$size")) { }
                }
            }
        }
    }

    private fun FlowContent.renderList(element: ParagraphContent.ItemList) {
        ul {
            element.items.forEach {
                li { renderText(it.content) }
            }
        }
    }

    private fun FlowContent.renderTable(table: ParagraphContent.Table) {
        // Small screen
        ul(classes("table")) {
            table.rows.forEach { row ->
                li {
                    div {
                        row.cells.forEachIndexed { index, cell ->
                            val spec = table.header.colSpec[index]
                            val textClasses = if (index == 0) classes("text-bold") else null

                            if (spec.headerContent.text.isEmpty()) {
                                div { span(textClasses) { renderTextWithoutStyle(cell.text) } }
                                div { }
                            } else {
                                div {
                                    span(textClasses) {
                                        renderTextWithoutStyle(spec.headerContent.text)
                                        text(":")
                                    }
                                }
                                div { span(textClasses) { renderTextWithoutStyle(cell.text) } }
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
                    table.header.colSpec.forEach {
                        th(classes = classes(alignmentClass(it.alignment))) {
                            colSpan = it.span.toString()
                            renderText(it.headerContent.text)
                        }
                    }
                }
            }
            tbody {
                table.rows.forEach { row ->
                    tr {
                        row.cells.forEachIndexed { index, cell ->
                            val spec = table.header.colSpec[index]
                            td(classes = classes(alignmentClass(spec.alignment))) {
                                colSpan = spec.span.toString()
                                renderText(cell.text)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun classes(vararg classes: String?): String =
        classes.filterNotNull().joinToString(" ") { "pensjonsbrev-$it" }

    private fun alignmentClass(alignment: ParagraphContent.Table.ColumnAlignment): String =
        when (alignment) {
            ParagraphContent.Table.ColumnAlignment.LEFT -> "text-left"
            ParagraphContent.Table.ColumnAlignment.RIGHT -> "text-right"
        }

    private fun FlowContent.renderSakspart(
        language: Language,
        felles: Felles,
    ) =
        div(classes("sakspart")) {
            with(felles.bruker) {
                val navnPrefix =
                    if (felles.vergeNavn != null) LanguageSetting.Sakspart.GJELDER_NAVN else LanguageSetting.Sakspart.NAVN

                listOfNotNull(
                    felles.vergeNavn?.let { LanguageSetting.Sakspart.VERGENAVN to it },
                    navnPrefix to fulltNavn(),
                    LanguageSetting.Sakspart.FOEDSELSNUMMER to foedselsnummer.value,
                    LanguageSetting.Sakspart.SAKSNUMMER to felles.saksnummer,
                )
            }.forEach {
                div(classes("sakspart-tittel")) { text(languageSettings.getSetting(language, it.first)) }
                div(classes("sakspart-verdi")) { text(it.second) }
            }
        }

    private fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find resource /$fileName""")
}
