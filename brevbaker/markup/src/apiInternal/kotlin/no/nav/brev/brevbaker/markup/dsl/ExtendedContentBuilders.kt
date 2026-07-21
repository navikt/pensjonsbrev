package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Tekstinnhold som også tillater [variable] og tags. Hvert element krever en eksplisitt [Int]-id –
 * denne modulen genererer aldri id-er.
 */
@MarkupDsl
class ExtendedContentBuilder internal constructor() : AbstractContentBuilder() {
    /**
     * Legg til fast tekst, valgfritt med [fontType] og [tags].
     *
     * ```
     * text(1, "Du får ")
     *
     * text(2, "fritekst", tags = setOf(ElementTags.FRITEKST))
     * ```
     */
    fun text(id: Int, text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Literal(id, text, fontType, tags))
    }

    /**
     * Legg til en variabel (datafelt som fylles ut ved rendring), valgfritt med [fontType] og [tags].
     *
     * ```
     * variable(2, "1000 kr", tags = setOf(ElementTags.REDIGERBAR_DATA))
     * ```
     */
    fun variable(id: Int, text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Variable(id, text, fontType, tags))
    }

    /**
     * Legg til et linjeskift.
     *
     * ```
     * newLine(3)
     * ```
     */
    fun newLine(id: Int) {
        texts.add(Text.NewLine(id))
    }
}

/**
 * Begrenset tekst-scope for overskrifter/ledetekster (titler, kolonneoverskrifter, form-choice-ledetekst).
 * Tillater kun ren tekst: [text] og [variable] (alltid [FontType.PLAIN], ingen linjeskift). Hvert element
 * krever en eksplisitt [Int]-id.
 */
@MarkupDsl
class PlainExtendedTextBuilder internal constructor() {
    internal val texts: MutableList<Text> = mutableListOf()

    /**
     * Vanlig tekst.
     *
     * ```
     * text(1, "Vedtak")
     * ```
     */
    fun text(id: Int, text: String) {
        texts.add(Text.Literal(id, text, FontType.PLAIN))
    }

    /**
     * Legg til en variabel.
     *
     * ```
     * variable(2, "navn")
     * ```
     */
    fun variable(id: Int, text: String) {
        texts.add(Text.Variable(id, text, FontType.PLAIN))
    }

    internal fun build(): List<Text> = texts.toList()
}

/** Ren tekst med `variable` (kun [Text.Literal]/[Text.Variable], [FontType.PLAIN], ingen linjeskift). */
internal fun plainExtendedText(build: PlainExtendedTextBuilder.() -> Unit): List<Text> =
    PlainExtendedTextBuilder().apply(build).build()
