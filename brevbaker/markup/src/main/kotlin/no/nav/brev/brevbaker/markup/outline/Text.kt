package no.nav.brev.brevbaker.markup.outline

import no.nav.brev.brevbaker.markup.MarkupInternalApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.Markup.Identifiable

/** Semantisk merkelapp på et tekst-element som styrer redigeringsatferden i skribenten. */
enum class EditBehaviour {
    FRITEKST,
    REDIGERBAR_DATA,
}

@Serializable
sealed class Text : Identifiable {
    abstract override val id: Int
    abstract val text: String
    abstract val fontType: FontType

    /**
     * Diskriminator som identifiserer tekst-typen. Ligger som en egen egenskap i modellen (og dermed i
     * JSON-en) slik at konsumenter kan deserialisere polymorft uten spesialoppsett.
     */
    abstract val type: Type

    enum class Type {
        LITERAL,
        VARIABLE,
        NEW_LINE,
    }

    enum class FontType { PLAIN, BOLD, ITALIC }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("LITERAL")
    data class Literal @MarkupInternalApi constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val editBehaviour: EditBehaviour? = null,
    ) : Text() {
        override val type: Type get() = Type.LITERAL
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("VARIABLE")
    data class Variable @MarkupInternalApi constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val editBehaviour: EditBehaviour? = null,
    ) : Text() {
        override val type: Type get() = Type.VARIABLE
    }

    @ConsistentCopyVisibility
    @Serializable
    @SerialName("NEW_LINE")
    data class NewLine @MarkupInternalApi constructor(
        override val id: Int,
    ) : Text() {
        override val type: Type get() = Type.NEW_LINE
        override val text: String get() = ""
        override val fontType: FontType get() = FontType.PLAIN
    }
}
