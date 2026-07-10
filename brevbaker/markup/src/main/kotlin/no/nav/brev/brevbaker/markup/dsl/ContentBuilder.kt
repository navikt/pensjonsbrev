package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en. Har [text] og [newLine], men ikke `variable` — de fleste
 * konsumenter trenger ikke variabler. Bruk [VariableContentBuilder] (via `*WithVariables`-inngangene) når du
 * trenger `variable`.
 */
@MarkupDsl
abstract class AbstractContentBuilder internal constructor(private val ids: IdGenerator) {
    protected val texts: MutableList<Text> = mutableListOf()

    protected fun nextId(): Int = ids.next()

    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        texts.add(Text.Literal(nextId(), text, fontType))
    }

    fun newLine() {
        texts.add(Text.NewLine(nextId()))
    }

    internal fun build(): List<Text> = texts.toList()
}

/** Tekstinnhold uten `variable`. */
@MarkupDsl
class ContentBuilder internal constructor(ids: IdGenerator) : AbstractContentBuilder(ids)

/** Tekstinnhold som også tillater [variable]. Beregnet på brevbaker/skribenten. */
@MarkupDsl
class VariableContentBuilder internal constructor(ids: IdGenerator) : AbstractContentBuilder(ids) {
    fun variable(text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Variable(nextId(), text, fontType, tags))
    }
}

/**
 * Begrenset tekst-scope for overskrifter/ledetekster (titler, kolonneoverskrifter, form-choice-ledetekst).
 * Tillater kun ren tekst: [text] og [variable] uten [FontType] (alltid [FontType.PLAIN]) og uten linjeskift.
 */
@MarkupDsl
class PlainVariableTextBuilder internal constructor(private val ids: IdGenerator) {
    private val texts: MutableList<Text> = mutableListOf()

    fun text(text: String) {
        texts.add(Text.Literal(ids.next(), text, FontType.PLAIN))
    }

    fun variable(text: String) {
        texts.add(Text.Variable(ids.next(), text, FontType.PLAIN))
    }

    internal fun build(): List<Text> = texts.toList()
}

/** Begrenset tekst-scope uten `variable` for plain-only overskrifter i [letterMarkup]. */
@MarkupDsl
class PlainTextBuilder internal constructor(private val ids: IdGenerator) {
    private val texts: MutableList<Text> = mutableListOf()

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

/** Ren tekst fra builder (kun [Text.Literal], [FontType.PLAIN], ingen linjeskift). */
internal fun IdGenerator.plainText(build: PlainTextBuilder.() -> Unit): List<Text> =
    PlainTextBuilder(this).apply(build).build()

/** Ren tekst med `variable` (kun [Text.Literal]/[Text.Variable], [FontType.PLAIN], ingen linjeskift). */
internal fun IdGenerator.plainVariableText(build: PlainVariableTextBuilder.() -> Unit): List<Text> =
    PlainVariableTextBuilder(this).apply(build).build()
