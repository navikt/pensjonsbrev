package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en.
 *
 * Tilgjengelige funksjoner:
 * - [text] for vanlig tekst
 * - [newLine] for linjeskift
 *
 * Bruk [ExtendedContentBuilder] (via `*Extended`-inngangene) når du også trenger [ExtendedContentBuilder.variable]
 * og støtte for metadata som tags.
 */
@MarkupDsl
abstract class AbstractContentBuilder internal constructor(private val ids: IdGenerator) {
    protected val texts: MutableList<Text> = mutableListOf()

    protected fun nextId(): Int = ids.next()

    /**
     * Legg til fast tekst i innholdet, valgfritt med [fontType].
     *
     * ```
     * text("Vanlig tekst ")
     * text("uthevet", FontType.BOLD)
     * ```
     */
    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        texts.add(Text.Literal(nextId(), text, fontType))
    }

    /**
     * Legg til et linjeskift i innholdet.
     *
     * Linjeskift bør kun brukes mellom tekst. Linjeskift på starten eller slutten av ett avsnitt fjernes automatisk.
     *
     * ```
     * text("Linje 1")
     * newLine()
     * text("Linje 2")
     * ```
     */
    fun newLine() {
        texts.add(Text.NewLine(nextId()))
    }

    internal fun build(): List<Text> = texts.toList()
}

/** Tekstinnhold uten `variable`. */
@MarkupDsl
class ContentBuilder internal constructor(ids: IdGenerator) : AbstractContentBuilder(ids)

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
     * Vanlig tittle.
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

/** Begrenset tekst-scope uten `variable` for plain-only overskrifter i [letterMarkup]. */
@MarkupDsl
class PlainTextBuilder internal constructor(private val ids: IdGenerator) {
    private val texts: MutableList<Text> = mutableListOf()

    /**
     * Legg til fast tekst i overskriften/ledeteksten.
     *
     * ```
     * text("Innledning")
     * ```
     */
    fun text(text: String) {
        texts.add(Text.Literal(ids.next(), text, FontType.PLAIN))
    }

    internal fun build(): List<Text> = texts.toList()
}

internal typealias ContentFactory<C> = (IdGenerator) -> C

internal fun <C : AbstractContentBuilder> IdGenerator.content(factory: ContentFactory<C>, build: C.() -> Unit): List<Text> =
    factory(this).apply(build).build()

/** Ren tekst fra en enkel [String] (kun [Text.Literal], [FontType.PLAIN]). */
internal fun IdGenerator.plainText(text: String): List<Text> = listOf(Text.Literal(next(), text, FontType.PLAIN))

/** Ren tekst fra DSL-blokk (kun [Text.Literal], [FontType.PLAIN], ingen linjeskift). */
internal fun IdGenerator.plainText(build: PlainTextBuilder.() -> Unit): List<Text> =
    PlainTextBuilder(this).apply(build).build()

/** Ren tekst med `variable` (kun [Text.Literal]/[Text.Variable], [FontType.PLAIN], ingen linjeskift). */
internal fun IdGenerator.plainExtendedText(build: PlainExtendedTextBuilder.() -> Unit): List<Text> =
    PlainExtendedTextBuilder(this).apply(build).build()
