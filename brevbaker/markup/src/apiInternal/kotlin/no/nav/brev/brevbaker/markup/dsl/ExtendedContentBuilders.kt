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
