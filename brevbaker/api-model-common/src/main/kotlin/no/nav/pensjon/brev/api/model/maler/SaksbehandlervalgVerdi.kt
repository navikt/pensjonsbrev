package no.nav.pensjon.brev.api.model.maler

sealed interface SaksbehandlervalgVerdi {
    enum class Type {
        BOOL, INTEGER, ENUM, TEXT
    }
    fun unwrap(): Any? = when (this) {
        is Bool -> bool
        is Integer -> int
        is Enum<*> -> enum
        is Text -> text
    }
    val type: Type
    val displayText: String

    class Bool(val bool: Boolean, override val displayText: String) : SaksbehandlervalgVerdi {
        override val type = Type.BOOL
        override fun toString() = "SaksbehandlervalgVerdi.Bool(bool=$bool)"
        override fun equals(other: Any?): Boolean {
            if (other !is Bool) return false
            return bool == other.bool
        }
        override fun hashCode() = Bool::class.java.hashCode() + bool.hashCode()
    }
    class Integer(val int: Int?, override val displayText: String) : SaksbehandlervalgVerdi {
        override val type = Type.INTEGER
        override fun toString() = "SaksbehandlervalgVerdi.Integer(int=$int)"
        override fun equals(other: Any?): Boolean {
            if (other !is Integer) return false
            return int == other.int
        }
        override fun hashCode() = Integer::class.java.hashCode() + (int?.hashCode() ?: 0)
    }
    class Enum<T : SaksbehandlerValgEnum>(val enum: T?, override val displayText: String, val clazz: Class<out kotlin.Enum<*>?>) : SaksbehandlervalgVerdi {
        override val type = Type.ENUM
        override fun toString() = "SaksbehandlervalgVerdi.Enum(enum=$enum)"
        override fun equals(other: Any?): Boolean {
            if (other !is Enum<*>) return false
            return enum == other.enum
        }
        override fun hashCode() = Enum::class.java.hashCode() + (enum?.hashCode() ?: 0)
        fun <V : kotlin.Enum<V>> kopier(enumverdi: kotlin.Enum<V>?) = Enum(
            enum = enumverdi as SaksbehandlerValgEnum?,
            displayText = displayText,
            clazz = clazz
        )
    }
    class Text(val text: String?, override val displayText: String) : SaksbehandlervalgVerdi {
        override val type = Type.TEXT
        override fun toString() = "SaksbehandlervalgVerdi.Text(text=$text)"
        override fun equals(other: Any?): Boolean {
            if (other !is Text) return false
            return text == other.text
        }
        override fun hashCode() = Text::class.java.hashCode() + (text?.hashCode() ?: 0)
    }
}

interface SaksbehandlerValgEnum {
    val displayText: String
}