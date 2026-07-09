package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * DSL-markør for de frittstående element-byggerne. Egen markør (ikke [no.nav.brev.brevbaker.markup.dsl])
 * slik at det ikke oppstår forveksling mellom brev-DSL-en og element-byggerne beregnet på enhetstester.
 */
@DslMarker
annotation class MarkupElementsDsl

/**
 * Scope for å bygge en liste av [Text]. Alle id-er defaulter til 0.
 */
@MarkupElementsDsl
class ElementContentScope internal constructor() {
    private val texts = mutableListOf<Text>()

    fun literal(text: String, id: Int = 0, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Literal(id, text, fontType, tags))
    }

    fun variable(text: String, id: Int = 0, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Variable(id, text, fontType, tags))
    }

    fun newLine(id: Int = 0) {
        texts.add(Text.NewLine(id))
    }

    internal fun build(): List<Text> = texts.toList()
}

internal fun elementContent(build: ElementContentScope.() -> Unit): List<Text> =
    ElementContentScope().apply(build).build()
