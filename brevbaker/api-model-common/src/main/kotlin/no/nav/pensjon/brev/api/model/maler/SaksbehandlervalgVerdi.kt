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

    class Bool(val bool: Boolean) : SaksbehandlervalgVerdi {
        override val type = Type.BOOL
    }
    class Integer(val int: Int?) : SaksbehandlervalgVerdi {
        override val type = Type.INTEGER
    }
    class Enum<T : SaksbehandlerValgEnum>(val enum: T?) : SaksbehandlervalgVerdi {
        override val type = Type.ENUM
    }
}

interface SaksbehandlerValgEnum {
    val displayText: String
}