package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Mellomrepresentasjon for én tekstbit på vei inn i den utvidede markup-DSL-en.
 *
 * Grunnen til at denne finnes i det hele tatt er at nabo-literaler *innenfor ett og samme
 * tekstuttrykk* skal slås sammen ([mergeAdjacentLiterals]) før de skrives.
 */
internal sealed interface TextFragment {
    val id: Int

    data class Literal(override val id: Int, val text: String, val fontType: FontType, val tags: Set<ElementTags>) : TextFragment

    data class Variable(override val id: Int, val text: String, val fontType: FontType) : TextFragment

    data class NewLine(override val id: Int) : TextFragment
}
