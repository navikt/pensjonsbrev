package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

@MarkupDsl
class ContentBuilder internal constructor(private val ids: IdGenerator) {
    private val texts = mutableListOf<Text>()

    fun literal(text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Literal(ids.next(), text, fontType, tags))
    }

    fun variable(text: String, fontType: FontType = FontType.PLAIN, tags: Set<ElementTags> = emptySet()) {
        texts.add(Text.Variable(ids.next(), text, fontType, tags))
    }

    fun newLine() {
        texts.add(Text.NewLine(ids.next()))
    }

    internal fun build(): List<Text> = texts.toList()
}

internal fun IdGenerator.content(build: ContentBuilder.() -> Unit): List<Text> =
    ContentBuilder(this).apply(build).build()
