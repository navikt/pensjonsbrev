package no.nav.brev.brevbaker.markup.outline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.Markup.Identifiable

/** Semantiske merkelapper på et tekst-element, brukt av redigeringsverktøy. */
enum class ElementTags {
    FRITEKST,
    REDIGERBAR_DATA,
}

@Serializable
sealed class Text : Identifiable {
    abstract override val id: Int
    abstract val text: String
    abstract val fontType: FontType

    enum class FontType { PLAIN, BOLD, ITALIC }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("LITERAL")
    data class Literal internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val tags: Set<ElementTags> = emptySet(),
    ) : Text()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("VARIABLE")
    data class Variable internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val tags: Set<ElementTags> = emptySet(),
    ) : Text()

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NEW_LINE")
    data class NewLine internal constructor(
        override val id: Int,
    ) : Text() {
        override val text: String get() = ""
        override val fontType: FontType get() = FontType.PLAIN
    }
}
