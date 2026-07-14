package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import kotlin.jvm.JvmName

/**
 * Bygg en [LetterMarkup] med utvidet DSL.
 *
 * Denne varianten støtter `variable(...)`, metadata (tags) og krever en eksplisitt id på hvert element.
 * Modulen genererer aldri id-er; kalleren (typisk `Letter2Markup` i core) må oppgi id-ene selv.
 *
 * ```
 * val brev = letterMarkupExtended {
 *     outline { paragraph(10) { text(11, "Du får "); variable(12, "uføretrygd") } }
 *     // ... saksinformasjon, signatur
 * }
 * ```
 */
fun letterMarkupExtended(build: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit): LetterMarkup =
    LetterMarkupBuilder(::ExtendedContentBuilder).apply(build).build()

/**
 * Som [attachment], men med støtte for `variable(...)`/tags og krav om eksplisitt id på hvert element.
 *
 * ```
 * val vedlegg = attachmentExtended(inkluderSaksinformasjon = false) {
 *     outline { paragraph(1) { text(2, "Sats: "); variable(3, "2G") } }
 * }
 * ```
 */
fun attachmentExtended(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ExtendedContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(::ExtendedContentBuilder, inkluderSaksinformasjon).apply(build).build()

/**
 * Som [pdfTittel], men med støtte for `variable(...)` og krav om eksplisitt id på hvert element.
 *
 * ```
 * val tittel = pdfTittelExtended { text(1, "Vedtak for "); variable(2, "navn") }
 * ```
 */
fun pdfTittelExtended(content: ExtendedContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(ExtendedContentBuilder().apply(content).build())

/**
 * Setter brevets hoved-tittel via DSL. Støtter også `variable`.
 *
 * ```
 * title1 { text(1, "Vedtak for "); variable(2, "navn") }
 * ```
 */
fun LetterMarkupBuilder<ExtendedContentBuilder>.title1(content: PlainExtendedTextBuilder.() -> Unit) =
    setTitle { plainExtendedText(content) }

/**
 * Legg til en nivå-2-overskrift via DSL-blokk med `variable`.
 *
 * ```
 * title2(5) { text(6, "Vedtak for "); variable(7, "navn") }
 * ```
 */
@JvmName("title2Extended")
fun OutlineBuilder<ExtendedContentBuilder>.title2(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(Block.Title2(id, plainExtendedText(content)))
}

/**
 * Legg til en nivå-3-overskrift via DSL-blokk med `variable`.
 *
 * ```
 * title3(5) { text(6, "Mellomtittel "); variable(7, "nr") }
 * ```
 */
@JvmName("title3Extended")
fun OutlineBuilder<ExtendedContentBuilder>.title3(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(Block.Title3(id, plainExtendedText(content)))
}

/**
 * Legg til en nivå-4-overskrift via DSL-blokk med `variable`.
 *
 * ```
 * title4(5) { text(6, "Detaljer "); variable(7, "nr") }
 * ```
 */
@JvmName("title4Extended")
fun OutlineBuilder<ExtendedContentBuilder>.title4(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(Block.Title4(id, plainExtendedText(content)))
}

/**
 * Legg til et avsnitt via DSL-blokk med `variable`.
 *
 * ```
 * paragraph(10) { text(11, "Du får "); variable(12, "uføretrygd") }
 * ```
 */
@JvmName("paragraphExtended")
fun OutlineBuilder<ExtendedContentBuilder>.paragraph(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    blocks.add(Block.Paragraph(id, contentFactory.content(content)))
}

/**
 * Legg til en punktliste (kulepunkter).
 *
 * ```
 * itemList(20) { item(21) { text(22, "Punkt 1") } }
 * ```
 */
@JvmName("itemListExtended")
fun OutlineBuilder<ExtendedContentBuilder>.itemList(id: Int, build: ItemsBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(Block.ItemList(id, ItemsBuilder(contentFactory).apply(build).build()))
}

/**
 * Legg til en nummerert liste.
 *
 * ```
 * numberedList(20) { item(21) { text(22, "Steg 1") } }
 * ```
 */
@JvmName("numberedListExtended")
fun OutlineBuilder<ExtendedContentBuilder>.numberedList(id: Int, build: ItemsBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(Block.NumberedList(id, ItemsBuilder(contentFactory).apply(build).build()))
}

/**
 * Legg til et listepunkt via DSL-blokk med `variable`.
 *
 * ```
 * item(21) { text(22, "Du får "); variable(23, "uføretrygd") }
 * ```
 */
@JvmName("itemExtended")
fun ItemsBuilder<ExtendedContentBuilder>.item(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    items.add(Block.Item(id, contentFactory.content(content)))
}

/**
 * Legg til en tabell med kolonneoverskrift og rader.
 *
 * ```
 * table(30) {
 *     header(31) { column(32, 33) { text(34, "Beløp") } }
 *     row(35) { cell(36) { text(37, "20 000 kr") } }
 * }
 * ```
 */
@JvmName("tableExtended")
fun OutlineBuilder<ExtendedContentBuilder>.table(id: Int, build: TableBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(TableBuilder(contentFactory).apply(build).build(id))
}

/**
 * Definer kolonnene i tabellen. Må brukes én gang per tabell, med minst én `column(...)`.
 *
 * ```
 * header(31) { column(32, 33) { text(34, "Beløp") } }
 * ```
 */
@JvmName("headerExtended")
fun TableBuilder<ExtendedContentBuilder>.header(id: Int, build: HeaderBuilder<ExtendedContentBuilder>.() -> Unit) {
    header = HeaderBuilder<ExtendedContentBuilder>().apply(build).build(id)
}

/**
 * Legg til én rad i tabellen. Antall celler må være lik antall kolonner i `header`.
 *
 * ```
 * row(35) { cell(36) { text(37, "A1") } }
 * ```
 */
@JvmName("rowExtended")
fun TableBuilder<ExtendedContentBuilder>.row(id: Int, build: RowBuilder<ExtendedContentBuilder>.() -> Unit) {
    rows.add(RowBuilder(contentFactory).apply(build).build(id))
}

/**
 * Legg til en kolonne i header med tekst som kan kombineres med `variable(...)`.
 * [id] er id-en til kolonnespesifikasjonen, [headerContentId] er id-en til overskriftscellen.
 *
 * ```
 * header(31) { column(32, 33) { text(34, "Beløp "); variable(35, "år") } }
 * ```
 */
fun HeaderBuilder<ExtendedContentBuilder>.column(
    id: Int,
    headerContentId: Int,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainExtendedTextBuilder.() -> Unit,
) {
    require(span >= 1) { "Table column span must be at least 1, but was $span" }
    val cell = Block.Table.Cell(headerContentId, plainExtendedText(content))
    colSpec.add(Block.Table.ColumnSpec(id, cell, alignment, span))
}

/**
 * Legg til en celle med sammensatt tekst.
 *
 * ```
 * row(35) { cell(36) { text(37, "Sum: "); variable(38, "beløp") } }
 * ```
 */
@JvmName("cellExtended")
fun RowBuilder<ExtendedContentBuilder>.cell(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    cells.add(Block.Table.Cell(id, contentFactory.content(content)))
}

/**
 * Legg til et skjemafelt for fritekst med angitt [size] og valgfri [vspace].
 *
 * ```
 * formText(40, Size.LONG) { text(41, "Skriv her") }
 * ```
 */
@JvmName("formTextExtended")
fun OutlineBuilder<ExtendedContentBuilder>.formText(id: Int, size: Size, vspace: Boolean = true, prompt: ExtendedContentBuilder.() -> Unit) {
    blocks.add(Block.FormText(id, contentFactory.content(prompt), size, vspace))
}

/**
 * Legg til et avkrysningsfelt med en ledetekst (`prompt`) og minst to valg (`choice`).
 *
 * ```
 * formChoice(50) { prompt { text(51, "Velg") }; choice(52) { text(53, "Ja") }; choice(54) { text(55, "Nei") } }
 * ```
 */
@JvmName("formChoiceExtended")
fun OutlineBuilder<ExtendedContentBuilder>.formChoice(id: Int, vspace: Boolean = true, build: FormChoiceBuilder<ExtendedContentBuilder>.() -> Unit) {
    val builder = FormChoiceBuilder(contentFactory)
    builder.vspace = vspace
    blocks.add(builder.apply(build).build(id))
}

/**
 * Sett avkrysningsfeltets ledetekst via DSL-blokk med `variable`.
 *
 * ```
 * formChoice(50) { prompt { text(51, "Svar innen "); variable(52, "frist") }; ... }
 * ```
 */
fun FormChoiceBuilder<ExtendedContentBuilder>.prompt(content: PlainExtendedTextBuilder.() -> Unit) {
    prompt.addAll(plainExtendedText(content))
}

/**
 * Legg til et svaralternativ via DSL-blokk med `variable`. Teksten må være ikke-tom.
 *
 * ```
 * choice(52) { text(53, "Ja, jeg samtykker") }
 * ```
 */
@JvmName("choiceExtended")
fun FormChoiceBuilder<ExtendedContentBuilder>.choice(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    val choiceContent = contentFactory.content(content)
    require(choiceContent.any { it.text.isNotBlank() }) { "Form choice option text must be non-empty" }
    choices.add(Block.FormChoice.Choice(id, choiceContent))
}
