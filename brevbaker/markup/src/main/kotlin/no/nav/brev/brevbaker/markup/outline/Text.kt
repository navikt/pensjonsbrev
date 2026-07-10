package no.nav.brev.brevbaker.markup.outline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.ElementTags

/**
 * Et tekst-element på laveste nivå: fast tekst ([Literal]), en variabel ([Variable]) eller et
 * linjeskift ([NewLine]). Hvert element har en unik [id] og en [type] for enkel oppslag uten `is`-sjekk.
 */
@Serializable
sealed class Text {
    abstract val id: Int
    abstract val text: String
    abstract val fontType: FontType
    abstract val type: Type

    enum class Type { LITERAL, VARIABLE, NEW_LINE }

    enum class FontType { PLAIN, BOLD, ITALIC }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("LITERAL")
    data class Literal internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val tags: Set<ElementTags> = emptySet(),
    ) : Text() {
        override val type: Type get() = Type.LITERAL
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("VARIABLE")
    data class Variable internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val tags: Set<ElementTags> = emptySet(),
    ) : Text() {
        override val type: Type get() = Type.VARIABLE
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NEW_LINE")
    data class NewLine internal constructor(
        override val id: Int,
    ) : Text() {
        override val text: String get() = ""
        override val fontType: FontType get() = FontType.PLAIN
        override val type: Type get() = Type.NEW_LINE
    }
}
