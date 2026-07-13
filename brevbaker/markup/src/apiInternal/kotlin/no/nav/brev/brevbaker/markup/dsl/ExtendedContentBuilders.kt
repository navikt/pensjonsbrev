package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/** Tekstinnhold som også tillater [variable] og tags. */
@MarkupDsl
class ExtendedContentBuilder internal constructor(ids: IdGenerator) : AbstractContentBuilder(ids) {
    /**
     * Legg til en variabel (datafelt som fylles ut ved rendring), valgfritt med [fontType] og [tags].
     *
     * ```
     * text("Du får ")
     * variable("1000 kr", tags = setOf(ElementTags.REDIGERBAR_DATA))
     * ```
     */
    fun variable(text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Variable(nextId(), text, fontType, tags))
    }
}

/**
 * Begrenset tekst-scope for overskrifter/ledetekster (titler, kolonneoverskrifter, form-choice-ledetekst).
 * Tillater kun ren tekst: [text] og [variable] uten [FontType] (alltid [FontType.PLAIN]) og uten linjeskift.
 */
@MarkupDsl
class PlainExtendedTextBuilder internal constructor(private val ids: IdGenerator) {
    private val texts: MutableList<Text> = mutableListOf()

    /**
     * Vanlig tekst.
     *
     * ```
     * text("Vedtak")
     * ```
     */
    fun text(text: String) {
        texts.add(Text.Literal(ids.next(), text, FontType.PLAIN))
    }

    /**
     * Legg til en variabel.
     *
     * ```
     * text("Vedtak for ")
     * variable("navn")
     * ```
     */
    fun variable(text: String) {
        texts.add(Text.Variable(ids.next(), text, FontType.PLAIN))
    }

    internal fun build(): List<Text> = texts.toList()
}

/** Ren tekst med `variable` (kun [Text.Literal]/[Text.Variable], [FontType.PLAIN], ingen linjeskift). */
internal fun IdGenerator.plainExtendedText(build: PlainExtendedTextBuilder.() -> Unit): List<Text> =
    PlainExtendedTextBuilder(this).apply(build).build()
