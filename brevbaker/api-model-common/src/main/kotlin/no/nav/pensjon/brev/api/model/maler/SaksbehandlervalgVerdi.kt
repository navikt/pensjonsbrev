package no.nav.pensjon.brev.api.model.maler

sealed interface SaksbehandlervalgVerdi {
    enum class Type {
        BOOL, INTEGER, ENUM
    }
    fun unwrap(): Any? = when (this) {
        is Bool -> bool
        is Integer -> int
        is Enum<*> -> enum
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
        override fun hashCode() = bool.hashCode()
    }
    class Integer(val int: Int?, override val displayText: String) : SaksbehandlervalgVerdi {
        override val type = Type.INTEGER
        override fun toString() = "SaksbehandlervalgVerdi.Integer(int=$int)"
        override fun equals(other: Any?): Boolean {
            if (other !is Integer) return false
            return int == other.int
        }
        override fun hashCode() = int.hashCode()
    }
    class Enum<T : SaksbehandlerValgEnum>(val enum: T?, override val displayText: String) : SaksbehandlervalgVerdi {
        override val type = Type.ENUM
        override fun toString() = "SaksbehandlervalgVerdi.Enum(enum=$enum)"
        override fun equals(other: Any?): Boolean {
            if (other !is Enum<*>) return false
            return enum == other.enum
        }
        override fun hashCode() = enum.hashCode()
    }
}

interface SaksbehandlerValgEnum {
    val displayText: String
}