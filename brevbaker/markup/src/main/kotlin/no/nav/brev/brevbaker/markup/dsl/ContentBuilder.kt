package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en. Har [literal] og [newLine], men ikke `variable` — de fleste
 * konsumenter trenger ikke variabler. Bruk [VariableContentBuilder] (via `*WithVariables`-inngangene) når du
 * trenger `variable`.
 */
@MarkupDsl
abstract class AbstractContentBuilder internal constructor(private val ids: IdGenerator) {
    protected val texts: MutableList<Text> = mutableListOf()

    protected fun nextId(): Int = ids.next()

    fun literal(text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Literal(nextId(), text, fontType, tags))
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

internal typealias ContentFactory<C> = (IdGenerator) -> C

internal fun <C : AbstractContentBuilder> IdGenerator.content(factory: ContentFactory<C>, build: C.() -> Unit): List<Text> =
    factory(this).apply(build).build()
