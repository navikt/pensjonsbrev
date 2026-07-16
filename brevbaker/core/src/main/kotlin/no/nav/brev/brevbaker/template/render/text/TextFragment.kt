package no.nav.brev.brevbaker.template.render.text

import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Mellomrepresentasjon for én tekstbit på vei inn i den utvidede markup-DSL-en.
 *
 * Grunnen til at denne finnes i det hele tatt er at nabo-literaler *innenfor ett og samme
 * tekstuttrykk* skal slås sammen ([mergeAdjacentLiterals]) før de skrives. DSL-byggeren er
 * append-only, så vi kan verken lese tilbake det som allerede er lagt inn eller konstruere
 * modulens [no.nav.brev.brevbaker.markup.outline.Text]-typer direkte. [TextFragment] er derfor
 * en kortlevd, ren datastruktur som kun brukes mellom «regn ut tekstbitene» og «skriv dem inn i
 * DSL-en» – den tres ikke gjennom outline/paragraf/tabell.
 */
internal sealed interface TextFragment {
    val id: Int

    /** Fast tekst. Kan slås sammen med en nabo-[Literal] så lenge begge er uten [tags]. */
    data class Literal(override val id: Int, val text: String, val fontType: FontType, val tags: Set<ElementTags>) : TextFragment

    /** Et datafelt som fylles ut ved rendring. Slås aldri sammen med naboer. */
    data class Variable(override val id: Int, val text: String, val fontType: FontType) : TextFragment

    /** Et linjeskift. */
    data class NewLine(override val id: Int) : TextFragment
}
