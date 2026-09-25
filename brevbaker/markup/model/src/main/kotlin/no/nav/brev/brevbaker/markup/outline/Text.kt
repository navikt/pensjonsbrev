package no.nav.brev.brevbaker.markup.outline

import no.nav.brev.brevbaker.markup.Markup.Identifiable
import java.util.Objects

/** Semantisk merkelapp på et tekst-element som styrer redigeringsatferden i skribenten. */
enum class EditBehaviour {
    FRITEKST,
    REDIGERBAR_DATA,
}
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

    class Literal internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val editBehaviour: EditBehaviour? = null,
    ) : Text() {
        override val type: Type get() = Type.LITERAL

        override fun equals(other: Any?) = other is Literal &&
                id == other.id &&
                text == other.text &&
                fontType == other.fontType &&
                editBehaviour == other.editBehaviour

        override fun hashCode() = Objects.hash(id, text, fontType, editBehaviour)
        override fun toString(): String =
            "Literal(id=$id, text=$text, fontType=$fontType, editBehaviour=$editBehaviour)"
    }

    class Variable internal constructor(
        override val id: Int,
        override val text: String,
        override val fontType: FontType = FontType.PLAIN,
        val editBehaviour: EditBehaviour? = null,
    ) : Text() {
        override val type: Type get() = Type.VARIABLE

        override fun equals(other: Any?) = other is Variable &&
                id == other.id &&
                text == other.text &&
                fontType == other.fontType &&
                editBehaviour == other.editBehaviour

        override fun hashCode() = Objects.hash(id, text, fontType, editBehaviour)
        override fun toString() = "Variable(id=$id, text=$text, fontType=$fontType, editBehaviour=$editBehaviour)"
    }

    class NewLine internal constructor(
        override val id: Int,
    ) : Text() {
        override val type: Type get() = Type.NEW_LINE
        override val text: String get() = ""
        override val fontType: FontType get() = FontType.PLAIN

        override fun equals(other: Any?) = other is NewLine && id == other.id
        override fun hashCode() = Objects.hash(id)
        override fun toString(): String = "NewLine(id=$id)"
    }
}
