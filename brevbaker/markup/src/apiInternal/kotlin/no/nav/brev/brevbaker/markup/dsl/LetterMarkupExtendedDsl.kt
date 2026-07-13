package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import kotlin.jvm.JvmName

/**
 * Bygg en [LetterMarkup] med utvidet DSL.
 *
 * Denne varianten støtter `variable(...)` i tekstinnhold, og metadata som tags på variabler som kun brukes i skribenten
 *
 * ```
 * val brev = letterMarkupExtended {
 *     outline { paragraph { text("Du får "); variable("uføretrygd") } }
 *     // ... saksinformasjon, signatur
 * }
 * ```
 */
fun letterMarkupExtended(build: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit): LetterMarkup =
    LetterMarkupBuilder(IdGenerator(), ::ExtendedContentBuilder).apply(build).build()

/**
 * Som [attachment], men med støtte for elementer som ikke brukes til rendring av pdf.
 *
 * ```
 * val vedlegg = attachmentExtended(inkluderSaksinformasjon = false) {
 *     outline { paragraph { text("Sats: "); variable("2G") } }
 * }
 * ```
 */
fun attachmentExtended(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ExtendedContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), ::ExtendedContentBuilder, inkluderSaksinformasjon).apply(build).build()

/**
 * Som [pdfTittel], men med støtte for elementer som ikke brukes til rendring av pdf.
 *
 * ```
 * val tittel = pdfTittelExtended { text("Vedtak for "); variable("navn") }
 * ```
 */
fun pdfTittelExtended(content: ExtendedContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(IdGenerator().content(::ExtendedContentBuilder, content))

/**
 *  Setter brevets hoved-tittel via dsl. Støtter også variable:
 * Se også shorthand-varianten `title1("...")`.
 *
 * ```
 * title1 { text("Vedtak for "); variable("navn") }
 * ```
 */
fun LetterMarkupBuilder<ExtendedContentBuilder>.title1(content: PlainExtendedTextBuilder.() -> Unit) =
    setTitle { plainExtendedText(content) }

/**
 * Legg til en nivå-2-overskrift via DSL-blokk med `variable`.
 * Se også kort-varianten `title2("...")`.
 *
 * ```
 * title2 { text("Vedtak for "); variable("navn") }
 * ```
 */
@JvmName("title2WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title2(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, ids.plainExtendedText(content))

/**
 * Legg til en nivå-3-overskrift via DSL-blokk med `variable`.
 * Se også kort-varianten `title3("...")`.
 *
 * ```
 * title3 { text("Mellomtittel "); variable("nr") }
 * ```
 */
@JvmName("title3WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title3(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, ids.plainExtendedText(content))

/**
 * Legg til en nivå-4-overskrift via DSL-blokk med `variable`.
 * Se også kort-varianten `title4("...")`.
 *
 * ```
 * title4 { text("Detaljer "); variable("nr") }
 * ```
 */
@JvmName("title4WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title4(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle4(this, ids.plainExtendedText(content))

/**
 * Legg til en kolonne i header med tekst som kan kombineres med `variable(...)`.
 * Se også kort-varianten `column("...")`.
 *
 * Parametere:
 * - `alignment`: justering av kolonneinnhold ([ColumnAlignment.LEFT] som standard)
 * - `span`: hvor mange kolonner overskriften skal dekke (`1` som standard). Brukes som forholdstall for hvor stor andel av bredden en kolonne skal bruke.
 *
 *   I de visuelle retningslinjene i Nav står det at:
 *  * Tekst er alltid venstrestilt for leseretning.
 *  * Tall høyrestilles.
 *  * I tabeller hvor kolonner blander tall og bokstaver bør det vurderes pr. tabell om innholdet skal venstrestilles eller høyrestilles.
 * ```
 * header { column { text("Beløp "); variable("år") } }
 * ```
 */
@JvmName("columnWithPlainExtendedTextBuilder")
fun HeaderBuilder<ExtendedContentBuilder>.column(
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainExtendedTextBuilder.() -> Unit,
) = HeaderBuilder.addColumn(this, alignment, span, ids.plainExtendedText(content))

/**
 * Sett avkrysningsfeltets ledetekst via DSL-blokk med `variable`.
 * Se også kort-varianten `prompt("...")`.
 *
 * ```
 * formChoice { prompt { text("Svar innen "); variable("frist") }; choice("Ja"); choice("Nei") }
 * ```
 */
fun FormChoiceBuilder<ExtendedContentBuilder>.prompt(content: PlainExtendedTextBuilder.() -> Unit) =
    FormChoiceBuilder.addPrompt(this, ids.plainExtendedText(content))
