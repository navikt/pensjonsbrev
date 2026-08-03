package no.nav.brev.brevbaker.markup.dsl.extended

import no.nav.brev.brevbaker.markup.dsl.content
import no.nav.brev.brevbaker.markup.dsl.AttachmentBuilder
import no.nav.brev.brevbaker.markup.dsl.LetterMarkupBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import no.nav.brev.brevbaker.markup.dsl.ItemsBuilder
import no.nav.brev.brevbaker.markup.dsl.TableBuilder
import no.nav.brev.brevbaker.markup.dsl.RowBuilder
import no.nav.brev.brevbaker.markup.dsl.HeaderBuilder
import no.nav.brev.brevbaker.markup.dsl.FormChoiceBuilder
import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.Saksinformasjon
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import kotlin.jvm.JvmName
import no.nav.brev.brevbaker.markup.MarkupModel

/**
 * Bygg en [LetterMarkup] med utvidet DSL.
 *
 * Denne varianten støtter `variable(...)`, redigeringsatferd (editBehaviour) og krever en eksplisitt id på hvert element.
 * Modulen genererer aldri id-er; kalleren (typisk `Letter2Markup` i core) må oppgi id-ene selv.
 *
 * ```
 * val brev = letterMarkupExtended(
 *     saksinformasjon = saksinformasjon(...),
 *     signatur = signatur(...),
 * ) {
 *     outline { paragraph(10) { text(11, "Du får "); variable(12, "uføretrygd") } }
 * }
 * ```
 */
@ExtendedMarkupDsl
fun letterMarkupExtended(
    saksinformasjon: Saksinformasjon,
    signatur: Signatur,
    init: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit,
): LetterMarkup =
    LetterMarkupBuilder(::ExtendedContentBuilder, saksinformasjon, signatur).apply(init).build()

/**
 * Som [attachment], men med støtte for `variable(...)`/tags og krav om eksplisitt id på hvert element.
 *
 * ```
 * val vedlegg = attachmentExtended(inkluderSaksinformasjon = false) {
 *     outline { paragraph(1) { text(2, "Sats: "); variable(3, "2G") } }
 * }
 * ```
 */
@ExtendedMarkupDsl
fun attachmentExtended(inkluderSaksinformasjon: Boolean = false, init: AttachmentBuilder<ExtendedContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(::ExtendedContentBuilder, inkluderSaksinformasjon).apply(init).build()

/**
 * Som [pdfTittel], men med støtte for `variable(...)` og krav om eksplisitt id på hvert element.
 *
 * ```
 * val tittel = pdfTittelExtended { text(1, "Vedtak for "); variable(2, "navn") }
 * ```
 */
@ExtendedMarkupDsl
fun pdfTittelExtended(content: ExtendedContentBuilder.() -> Unit): PDFTittel =
    MarkupModel.pdfTittel(ExtendedContentBuilder().apply(content).build())

/**
 * Setter brevets hoved-tittel via DSL. Støtter også `variable`.
 *
 * ```
 * title1 { text(1, "Vedtak for "); variable(2, "navn") }
 * ```
 */
@ExtendedMarkupDsl
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
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.title2(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(MarkupModel.title2(id, plainExtendedText(content)))
}

/**
 * Legg til en nivå-3-overskrift via DSL-blokk med `variable`.
 *
 * ```
 * title3(5) { text(6, "Mellomtittel "); variable(7, "nr") }
 * ```
 */
@JvmName("title3Extended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.title3(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(MarkupModel.title3(id, plainExtendedText(content)))
}

/**
 * Legg til en nivå-4-overskrift via DSL-blokk med `variable`.
 *
 * ```
 * title4(5) { text(6, "Detaljer "); variable(7, "nr") }
 * ```
 */
@JvmName("title4Extended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.title4(id: Int, content: PlainExtendedTextBuilder.() -> Unit) {
    blocks.add(MarkupModel.title4(id, plainExtendedText(content)))
}

/**
 * Legg til et avsnitt via DSL-blokk med `variable`.
 *
 * ```
 * paragraph(10) { text(11, "Du får "); variable(12, "uføretrygd") }
 * ```
 */
@JvmName("paragraphExtended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.paragraph(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    blocks.add(MarkupModel.paragraph(id, contentFactory.content(content)))
}

/**
 * Legg til en punktliste (kulepunkter).
 *
 * ```
 * itemList(20) { item(21) { text(22, "Punkt 1") } }
 * ```
 */
@JvmName("itemListExtended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.itemList(id: Int, init: ItemsBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(MarkupModel.itemList(id, ItemsBuilder(contentFactory).apply(init).build()))
}

/**
 * Legg til en nummerert liste.
 *
 * ```
 * numberedList(20) { item(21) { text(22, "Steg 1") } }
 * ```
 */
@JvmName("numberedListExtended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.numberedList(id: Int, init: ItemsBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(MarkupModel.numberedList(id, ItemsBuilder(contentFactory).apply(init).build()))
}

/**
 * Legg til et listepunkt via DSL-blokk med `variable`.
 *
 * ```
 * item(21) { text(22, "Du får "); variable(23, "uføretrygd") }
 * ```
 */
@JvmName("itemExtended")
@ExtendedMarkupDsl
fun ItemsBuilder<ExtendedContentBuilder>.item(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    items.add(MarkupModel.item(id, contentFactory.content(content)))
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
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.table(id: Int, init: TableBuilder<ExtendedContentBuilder>.() -> Unit) {
    blocks.add(TableBuilder(contentFactory).apply(init).build(id))
}

/**
 * Definer kolonnene i tabellen. Må brukes én gang per tabell, med minst én `column(...)`.
 *
 * ```
 * header(31) { column(32, 33) { text(34, "Beløp") } }
 * ```
 */
@JvmName("headerExtended")
@ExtendedMarkupDsl
fun TableBuilder<ExtendedContentBuilder>.header(id: Int, init: HeaderBuilder<ExtendedContentBuilder>.() -> Unit) {
    header = HeaderBuilder<ExtendedContentBuilder>().apply(init).build(id)
}

/**
 * Legg til én rad i tabellen. Antall celler må være lik antall kolonner i `header`.
 *
 * ```
 * row(35) { cell(36) { text(37, "A1") } }
 * ```
 */
@JvmName("rowExtended")
@ExtendedMarkupDsl
fun TableBuilder<ExtendedContentBuilder>.row(id: Int, init: RowBuilder<ExtendedContentBuilder>.() -> Unit) {
    rows.add(RowBuilder(contentFactory).apply(init).build(id))
}

/**
 * Legg til en kolonne i header med tekst som kan kombineres med `variable(...)`.
 * [id] er id-en til kolonnespesifikasjonen.
 *
 * ```
 * header(31) { column(32) { text(34, "Beløp "); variable(35, "år") } }
 * ```
 */
@ExtendedMarkupDsl
fun HeaderBuilder<ExtendedContentBuilder>.column(
    id: Int,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainExtendedTextBuilder.() -> Unit,
) {
    require(span >= 1) { "Table column span must be at least 1, but was $span" }
    colSpec.add(MarkupModel.columnSpec(id, plainExtendedText(content), alignment, span))
}

/**
 * Legg til en celle med sammensatt tekst.
 *
 * ```
 * row(35) { cell(36) { text(37, "Sum: "); variable(38, "beløp") } }
 * ```
 */
@JvmName("cellExtended")
@ExtendedMarkupDsl
fun RowBuilder<ExtendedContentBuilder>.cell(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    cells.add(MarkupModel.cell(id, contentFactory.content(content)))
}

/**
 * Legg til et skjemafelt for fritekst med angitt [size] og valgfri [vspace].
 *
 * ```
 * formText(40, Size.LONG) { text(41, "Skriv her") }
 * ```
 */
@JvmName("formTextExtended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.formText(id: Int, size: Size, vspace: Boolean = true, prompt: ExtendedContentBuilder.() -> Unit) {
    blocks.add(MarkupModel.formText(id, contentFactory.content(prompt), size, vspace))
}

/**
 * Legg til et avkrysningsfelt med en ledetekst (`prompt`) og minst to valg (`choice`).
 *
 * ```
 * formChoice(50) { prompt { text(51, "Velg") }; choice(52) { text(53, "Ja") }; choice(54) { text(55, "Nei") } }
 * ```
 */
@JvmName("formChoiceExtended")
@ExtendedMarkupDsl
fun OutlineBuilder<ExtendedContentBuilder>.formChoice(id: Int, vspace: Boolean = true, init: FormChoiceBuilder<ExtendedContentBuilder>.() -> Unit) {
    val builder = FormChoiceBuilder(contentFactory)
    builder.vspace = vspace
    blocks.add(builder.apply(init).build(id))
}

/**
 * Sett avkrysningsfeltets ledetekst via DSL-blokk med `variable`.
 *
 * ```
 * formChoice(50) { prompt { text(51, "Svar innen "); variable(52, "frist") }; ... }
 * ```
 */
@ExtendedMarkupDsl
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
@ExtendedMarkupDsl
fun FormChoiceBuilder<ExtendedContentBuilder>.choice(id: Int, content: ExtendedContentBuilder.() -> Unit) {
    val choiceContent = contentFactory.content(content)
    require(choiceContent.any { it.text.isNotBlank() }) { "Form choice option text must be non-empty" }
    choices.add(MarkupModel.choice(id, choiceContent))
}
