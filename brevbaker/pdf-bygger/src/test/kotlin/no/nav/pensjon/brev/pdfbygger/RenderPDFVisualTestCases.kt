@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import no.nav.brev.brevbaker.markup.dsl.cell
import no.nav.brev.brevbaker.markup.dsl.choice
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.formChoice
import no.nav.brev.brevbaker.markup.dsl.formText
import no.nav.brev.brevbaker.markup.dsl.header
import no.nav.brev.brevbaker.markup.dsl.item
import no.nav.brev.brevbaker.markup.dsl.itemList
import no.nav.brev.brevbaker.markup.dsl.letterMarkup as letterMarkupV2
import no.nav.brev.brevbaker.markup.dsl.letterPDFRequest
import no.nav.brev.brevbaker.markup.dsl.numberedList
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.prompt
import no.nav.brev.brevbaker.markup.dsl.row
import no.nav.brev.brevbaker.markup.dsl.saksinformasjon
import no.nav.brev.brevbaker.markup.dsl.signatur
import no.nav.brev.brevbaker.markup.dsl.table
import no.nav.brev.brevbaker.markup.dsl.title1 as v2Title1
import no.nav.brev.brevbaker.markup.dsl.title2 as v2Title2
import no.nav.brev.brevbaker.markup.dsl.title3 as v2Title3
import no.nav.brev.brevbaker.markup.dsl.title4 as v2Title4
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size as V2FormSize
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment as V2Align
import no.nav.brev.brevbaker.markup.outline.Text.FontType as V2FontType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form.Text.Size as V1FormSize
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.ColumnAlignment as V1Align
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType as V1FontType
import java.time.LocalDate

/**
 * A visual/compatibility test case defined once and rendered two ways: as a legacy [PDFRequest]
 * (api-model `LetterMarkup`, [v1]) and as a [LetterPDFRequest] (markup v2 model, [v2]).
 *
 * Both representations describe the same logical letter, so the pdf-bygger v1 and v2 renderers must
 * produce byte-identical PDFs (verified by `RenderPDFMarkupCompatibilityITest`). Note the deliberate
 * title-level offset between the models: api-model `title1/2/3` map to the same Typst levels as
 * markup v2 `title2/3/4`, and the letter's main title (`title { }` / `title1(...)`) maps to the
 * document title.
 */
data class RenderPDFVisualTestCase(
    val testName: String,
    private val title: String = testName,
    private val annenMottakerNavn: String? = null,
    private val saksbehandlerNavn: String? = null,
    private val attesterendeSaksbehandlerNavn: String? = null,
    private val attachments: List<CaseAttachment> = emptyList(),
    private val elements: List<CaseElement> = emptyList(),
) {
    fun v1(): PDFRequest {
        val letter = letterMarkup {
            title { text(this@RenderPDFVisualTestCase.title) }
            sakspart { annenMottakerNavn = this@RenderPDFVisualTestCase.annenMottakerNavn }
            signatur {
                saksbehandlerNavn = this@RenderPDFVisualTestCase.saksbehandlerNavn
                attesterendeSaksbehandlerNavn = this@RenderPDFVisualTestCase.attesterendeSaksbehandlerNavn
            }
            outline { elements.forEach { it.v1(this) } }
        }
        return PDFRequest(
            letterMarkup = letter,
            attachments = attachments.map { it.v1() },
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    }

    fun v2(): LetterPDFRequest {
        val letter = letterMarkupV2(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Navn Navnesen",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "123",
                dokumentDato = LocalDate.of(2025, 1, 1),
                annenMottakerNavn = annenMottakerNavn,
            ),
            signatur = signatur(
                hilsenTekst = "hilsen",
                navAvsenderEnhet = "Nav sentralt",
                saksbehandlerNavn = saksbehandlerNavn,
                attesterendeSaksbehandlerNavn = attesterendeSaksbehandlerNavn,
            ),
        ) {
            v2Title1(this@RenderPDFVisualTestCase.title)
            outline { elements.forEach { it.v2(this) } }
        }
        return letterPDFRequest(Markup.Spraak.BOKMAL, Markup.Brevtype.VEDTAKSBREV, letter) {
            attachments.forEach { it.applyV2(this) }
        }
    }

    override fun toString(): String = testName
}

/** A shared attachment definition, rendered to both the v1 and v2 models. */
class CaseAttachment(
    private val title: String,
    private val includeSakspart: Boolean,
    private val elements: List<CaseElement>,
) {
    fun v1(): no.nav.pensjon.brevbaker.api.model.LetterMarkup.Attachment =
        attachment {
            title { text(this@CaseAttachment.title) }
            includeSakspart = this@CaseAttachment.includeSakspart
            outline { elements.forEach { it.v1(this) } }
        }

    fun applyV2(builder: no.nav.brev.brevbaker.markup.dsl.PDFRequestBuilder) {
        builder.attachment(inkluderSaksinformasjon = includeSakspart) {
            v2Title1(this@CaseAttachment.title)
            outline { elements.forEach { it.v2(this) } }
        }
    }
}

internal object RenderPDFVisualTestCases {
    val allCases: List<RenderPDFVisualTestCase> =
        buildList {
            add(
                case("two paragraphs in a row") {
                    paragraph(loremIpsum)
                    paragraph(loremIpsum)
                }
            )
            add(
                case("fonts") {
                    paragraph(
                        run("The quick brown fox jumps over the lazy dog. ", Font.PLAIN),
                        run("The quick brown fox jumps over the lazy dog. ", Font.ITALIC),
                        run("The quick brown fox jumps over the lazy dog. ", Font.BOLD),
                    )
                }
            )
            add(
                case("bindestrek") {
                    paragraph("https://www.nav.no/ufore-ettersende-post")
                    val testString = (50..200).joinToString(" ") {
                        Char(it) + "-" + Char(it + 1)
                    }
                    paragraph(testString)
                }
            )
            add(
                case(
                    testName = "verge foersteside",
                    annenMottakerNavn = "Verge vergeson",
                    saksbehandlerNavn = "Saksbehandler Saksbehandlersen",
                ) {
                    title1("First title")
                    paragraph(loremIpsum)
                }
            )
            add(
                case("form choice med vspace") {
                    title1("Form choice")
                    formChoice("Hvor lenge har du jobba?", vspace = true, "0 år", "1 år", "2 til 5 år", "6 år eller mer")
                }
            )
            add(
                case("title should not be put on a separate page") {
                    repeat(14) { paragraph("Padding") }
                    title1("Title 1")
                    title2("Title 2")
                    title3("Title 3")
                }
            )
            add(
                case("form choice uten vspace") {
                    title1("Form choice uten vspace")
                    formChoice("Hvor lenge vil du jobbe?", vspace = false, "0 år", "1 år", "2 til 5 år", "6 år eller mer")
                }
            )
            add(
                case("short form text med vspace") {
                    title1("Form text short med vspace")
                    formText("test", FormSize.SHORT, vspace = true)
                }
            )
            add(
                case("long form text med vspace") {
                    title1("Form text long med vspace")
                    formText("test", FormSize.LONG, vspace = true)
                }
            )
            add(
                case("short form text uten vspace") {
                    title1("Form text short uten vspace")
                    formText("test", FormSize.SHORT, vspace = false)
                }
            )
            add(
                case("long form text uten vspace") {
                    title1("Form text long uten vspace")
                    formText("test", FormSize.LONG, vspace = false)
                }
            )
            add(
                vedleggCase(
                    testName = "verge vedlegg med saksinfo",
                    includeSakspart = true,
                    annenMottakerNavn = "Verge vergeson",
                    saksbehandlerNavn = "Saksbehandler Saksbehandlersen",
                ) {
                    title1("First title")
                    paragraph(loremIpsum)
                }
            )
            add(
                vedleggCase(
                    testName = "vedlegg med saksinfo",
                    includeSakspart = true,
                ) {
                    title1("First title")
                    paragraph(loremIpsum)
                }
            )
            add(
                vedleggCase(
                    testName = "vedlegg uten saksinfo",
                    includeSakspart = false,
                ) {
                    title1("First title")
                    paragraph(loremIpsum)
                }
            )
            add(
                case(
                    testName = "brev med saksbehandler underskrift",
                    saksbehandlerNavn = "Ole Saksbehandler",
                ) {
                    paragraph(loremIpsum)
                }
            )
            add(
                case(
                    testName = "brev med saksbehandler og attestant underskrift",
                    saksbehandlerNavn = "Ole Saksbehandler",
                    attesterendeSaksbehandlerNavn = "Per Saksbehandler",
                ) {
                    paragraph(loremIpsum)
                }
            )
            add(
                case(
                    testName = "test av ulike ",
                    saksbehandlerNavn = "Ole Saksbehandler",
                    attesterendeSaksbehandlerNavn = "Per Saksbehandler",
                ) {
                    paragraph(loremIpsum)
                }
            )
            add(
                case(
                    testName = "vedtaksbrev med saksbehandler underskrift",
                    saksbehandlerNavn = "Ole Saksbehandler",
                    attesterendeSaksbehandlerNavn = "Per Attesterende",
                ) {
                    paragraph(loremIpsum)
                }
            )
            add(
                case("should not add extra line change when nearing end of line before shipping content to new line") {
                    paragraph("Denne testen skal vise at det ikke fremstår en bug hvor det kommer ekstra linjeskift under innhold som tar opp hele bredden av linjen.")
                    repeat(30) {
                        paragraph("Denne linjen skal ikke ha større mellomrom til neste setning enn de andre. Dette er littegranne filler ˌˌˌˌˌˌ" + "ˌ".repeat(it))
                    }
                }
            )
            add(
                case("Table across multiple pages") {
                    table(
                        columns = listOf(
                            Col("Tekst", Align.LEFT, span = 2),
                            Col("Kroner", Align.RIGHT),
                        ),
                        rows = (1..100).map { listOf("Rad $it", "$it Kroner") },
                    )
                }
            )
            add(
                case("Table title should have the correct spacing when sticking to a table that was shipped to the next page") {
                    repeat(5) { paragraph("Padding text bla bla. ".repeat(18)) }
                    paragraph("Padding text bla bla. ".repeat(14))
                    title1("Test-tittel")
                    testTable(5)
                }
            )
            add(
                case("table title and other content") {
                    paragraph("asdf")
                    title1("Test-tittel")
                    testTable(5)
                }
            )
            add(
                case("table title and other title") {
                    title1("Test-tittel")
                    title1("Test-tittel")
                    testTable(5)
                }
            )
            add(
                case("small text then bullet list") {
                    paragraphThenItemList("Dette er en liste:", "Bullet point 1", "Bullet point 2")
                }
            )
            ElementType.entries.forEach { elementA ->
                ElementType.entries.forEach { elementB ->
                    add(
                        case("${elementA.description} then ${elementB.description}") {
                            outlineElementOfType(elementA)
                            outlineElementOfType(elementB)
                        }
                    )
                }
            }
        }

    private fun case(
        testName: String,
        title: String = testName,
        annenMottakerNavn: String? = null,
        saksbehandlerNavn: String? = null,
        attesterendeSaksbehandlerNavn: String? = null,
        build: CaseBuilder.() -> Unit,
    ) = RenderPDFVisualTestCase(
        testName = testName,
        title = title,
        annenMottakerNavn = annenMottakerNavn,
        saksbehandlerNavn = saksbehandlerNavn,
        attesterendeSaksbehandlerNavn = attesterendeSaksbehandlerNavn,
        elements = CaseBuilder().apply(build).build(),
    )

    private fun vedleggCase(
        testName: String,
        includeSakspart: Boolean,
        title: String = testName,
        annenMottakerNavn: String? = null,
        saksbehandlerNavn: String? = null,
        build: CaseBuilder.() -> Unit,
    ): RenderPDFVisualTestCase = RenderPDFVisualTestCase(
        testName = testName,
        title = title,
        annenMottakerNavn = annenMottakerNavn,
        saksbehandlerNavn = saksbehandlerNavn,
        attachments = listOf(
            CaseAttachment(
                title = title,
                includeSakspart = includeSakspart,
                elements = CaseBuilder().apply(build).build(),
            )
        ),
    )

    private fun CaseBuilder.testTable(numRows: Int) {
        table(
            columns = listOf(Col("Column A"), Col("Column B")),
            rows = (1..numRows).map { listOf("Cell A-$it", "Cell B-$it") },
        )
    }

    private fun CaseBuilder.outlineElementOfType(type: ElementType) {
        when (type) {
            ElementType.T1 -> title1("First title")
            ElementType.T2 -> title2("Second title")
            ElementType.T3 -> title3("Third title")
            ElementType.PAR -> paragraph(loremIpsum)
            ElementType.TABLE -> testTable(1)
            ElementType.LIST -> itemList("Bullet point 1", "Bullet point 2")
            ElementType.NUMBERED_LIST -> numberedList("Numbered point 1", "Numbered point 2")
            ElementType.FORM -> formText("Underskrift:", FormSize.FILL, vspace = false)
        }
    }

    private enum class ElementType(val description: String) {
        T1("Title 1"),
        T2("Title 2"),
        T3("Title 3"),
        PAR("Paragraph"),
        TABLE("Table"),
        LIST("Item list"),
        FORM("Form"),
        NUMBERED_LIST("Numbered list")
    }
}

/** Font types available across both markup models. */
internal enum class Font { PLAIN, BOLD, ITALIC }

/** Form-field sizes available across both markup models. */
internal enum class FormSize { SHORT, LONG, FILL }

/** Column alignments available across both markup models. */
internal enum class Align { LEFT, RIGHT }

private fun Font.v1(): V1FontType = when (this) {
    Font.PLAIN -> V1FontType.PLAIN
    Font.BOLD -> V1FontType.BOLD
    Font.ITALIC -> V1FontType.ITALIC
}

private fun Font.v2(): V2FontType = when (this) {
    Font.PLAIN -> V2FontType.PLAIN
    Font.BOLD -> V2FontType.BOLD
    Font.ITALIC -> V2FontType.ITALIC
}

private fun FormSize.v1(): V1FormSize = when (this) {
    FormSize.SHORT -> V1FormSize.SHORT
    FormSize.LONG -> V1FormSize.LONG
    FormSize.FILL -> V1FormSize.FILL
}

private fun FormSize.v2(): V2FormSize = when (this) {
    FormSize.SHORT -> V2FormSize.SHORT
    FormSize.LONG -> V2FormSize.LONG
    FormSize.FILL -> V2FormSize.FILL
}

private fun Align.v1(): V1Align = when (this) {
    Align.LEFT -> V1Align.LEFT
    Align.RIGHT -> V1Align.RIGHT
}

private fun Align.v2(): V2Align = when (this) {
    Align.LEFT -> V2Align.LEFT
    Align.RIGHT -> V2Align.RIGHT
}

/** A run of text with a font, for mixed-font paragraphs. */
internal data class TextRun(val text: String, val font: Font)

internal fun run(text: String, font: Font = Font.PLAIN): TextRun = TextRun(text, font)

/** A table header column definition, shared across both markup models. */
internal data class Col(val text: String, val alignment: Align = Align.LEFT, val span: Int = 1)

/**
 * A single block-level element defined once and rendered to both markup models. All elements map to
 * byte-identical Typst output between the pdf-bygger v1 and v2 renderers.
 */
sealed interface CaseElement {
    fun v1(builder: LetterMarkupBlocksBuilder)
    fun v2(builder: OutlineBuilder<ContentBuilder>)
}

private class Title1Element(val text: String) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.title1 { text(this@Title1Element.text) }
    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.v2Title2(text)
}

private class Title2Element(val text: String) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.title2 { text(this@Title2Element.text) }
    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.v2Title3(text)
}

private class Title3Element(val text: String) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.title3 { text(this@Title3Element.text) }
    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.v2Title4(text)
}

private class ParagraphElement(val runs: List<TextRun>) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        runs.forEach { text(it.text, it.font.v1()) }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.paragraph {
        runs.forEach { text(it.text, it.font.v2()) }
    }
}

private class ParagraphThenItemListElement(val text: String, val items: List<String>) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        text(this@ParagraphThenItemListElement.text)
        list { items.forEach { i -> item { text(i) } } }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) {
        builder.paragraph(text)
        builder.itemList { items.forEach { i -> item(i) } }
    }
}

private class ItemListElement(val items: List<String>, val numbered: Boolean) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        if (numbered) numberedList { items.forEach { i -> item { text(i) } } }
        else list { items.forEach { i -> item { text(i) } } }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) {
        if (numbered) builder.numberedList { items.forEach { i -> item(i) } }
        else builder.itemList { items.forEach { i -> item(i) } }
    }
}

private class FormChoiceElement(
    val prompt: String,
    val vspace: Boolean,
    val choices: List<String>,
) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        formChoice(vspace = vspace, prompt = { text(prompt) }) {
            choices.forEach { c -> choice { text(c) } }
        }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.formChoice(vspace = vspace) {
        prompt(prompt)
        choices.forEach { c -> choice(c) }
    }
}

private class FormTextElement(
    val prompt: String,
    val size: FormSize,
    val vspace: Boolean,
) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        formText(size.v1(), vspace = vspace) { text(prompt) }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.formText(size.v2(), vspace = vspace) {
        text(prompt)
    }
}

private class TableElement(val columns: List<Col>, val rows: List<List<String>>) : CaseElement {
    override fun v1(builder: LetterMarkupBlocksBuilder) = builder.paragraph {
        table(header = {
            columns.forEach { c -> column(span = c.span, alignment = c.alignment.v1()) { text(c.text) } }
        }) {
            rows.forEach { r -> row { r.forEach { cellText -> cell { text(cellText) } } } }
        }
    }

    override fun v2(builder: OutlineBuilder<ContentBuilder>) = builder.table {
        header { columns.forEach { c -> column(c.text, c.alignment.v2(), c.span) } }
        rows.forEach { r -> row { r.forEach { cellText -> cell(cellText) } } }
    }
}

/** Small DSL used to declare a case's blocks once; each call records a shared [CaseElement]. */
internal class CaseBuilder {
    private val elements = mutableListOf<CaseElement>()

    fun paragraph(text: String, font: Font = Font.PLAIN) {
        elements.add(ParagraphElement(listOf(TextRun(text, font))))
    }

    fun paragraph(vararg runs: TextRun) {
        elements.add(ParagraphElement(runs.toList()))
    }

    fun title1(text: String) = elements.add(Title1Element(text))
    fun title2(text: String) = elements.add(Title2Element(text))
    fun title3(text: String) = elements.add(Title3Element(text))

    fun itemList(vararg items: String) = elements.add(ItemListElement(items.toList(), numbered = false))
    fun numberedList(vararg items: String) = elements.add(ItemListElement(items.toList(), numbered = true))

    fun paragraphThenItemList(text: String, vararg items: String) {
        elements.add(ParagraphThenItemListElement(text, items.toList()))
    }

    fun formChoice(prompt: String, vspace: Boolean, vararg choices: String) {
        elements.add(FormChoiceElement(prompt, vspace, choices.toList()))
    }

    fun formText(prompt: String, size: FormSize, vspace: Boolean) {
        elements.add(FormTextElement(prompt, size, vspace))
    }

    fun table(columns: List<Col>, rows: List<List<String>>) {
        elements.add(TableElement(columns, rows))
    }

    fun build(): List<CaseElement> = elements.toList()
}

private val loremIpsum =
    "Fusce sed diam ac dui luctus venenatis sit amet sit amet sapien. Praesent non congue metus. " +
        "Morbi rutrum pellentesque rhoncus. Duis semper dictum rutrum. Curabitur iaculis, magna sit amet " +
        "varius dignissim, sapien augue pellentesque mi, id malesuada arcu risus et justo. Vivamus fermentum " +
        "neque ac purus faucibus, non viverra massa pulvinar. Suspendisse ornare erat hendrerit, condimentum " +
        "lorem vel, fringilla dui. Donec non tortor dignissim, ornare metus nec, malesuada nulla. Nulla " +
        "convallis arcu ultricies augue consectetur, eu mattis neque tristique. Sed suscipit lacus vel risus " +
        "lobortis, sed dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. " +
        "Donec elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim " +
        "rhoncus consequat. Vivamus laoreet semper facilisis."
