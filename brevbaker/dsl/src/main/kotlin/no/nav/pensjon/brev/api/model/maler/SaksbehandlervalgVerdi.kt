package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.InternKonstruktoer

sealed interface SaksbehandlervalgVerdi<out T> {
    enum class Type {
        BOOL, INTEGER, ENUM, TEXT
    }

    val defaultValue: T
    val type: Type
    val displayText: String

    class Bool @InternKonstruktoer constructor(override val defaultValue: Boolean, override val displayText: String) : SaksbehandlervalgVerdi<Boolean> {
        override val type = Type.BOOL
        override fun toString() = "SaksbehandlervalgVerdi.Bool(bool=$defaultValue)"
        override fun equals(other: Any?): Boolean {
            if (other !is Bool) return false
            return defaultValue == other.defaultValue
        }

        override fun hashCode() = Bool::class.java.hashCode() + defaultValue.hashCode()
    }

    class Integer @InternKonstruktoer constructor(override val defaultValue: Int?, override val displayText: String) : SaksbehandlervalgVerdi<Int?> {
        override val type = Type.INTEGER
        override fun toString() = "SaksbehandlervalgVerdi.Integer(int=$defaultValue)"
        override fun equals(other: Any?): Boolean {
            if (other !is Integer) return false
            return defaultValue == other.defaultValue
        }

        override fun hashCode() = Integer::class.java.hashCode() + (defaultValue?.hashCode() ?: 0)
    }

    class Enum<T : SaksbehandlerValgEnum> @InternKonstruktoer constructor(override val defaultValue: T?, override val displayText: String, val clazz: Class<out kotlin.Enum<*>?>) : SaksbehandlervalgVerdi<T?> {
        override val type = Type.ENUM
        override fun toString() = "SaksbehandlervalgVerdi.Enum(enum=$defaultValue)"
        override fun equals(other: Any?): Boolean {
            if (other !is Enum<*>) return false
            return defaultValue == other.defaultValue
        }

        override fun hashCode() = Enum::class.java.hashCode() + (defaultValue?.hashCode() ?: 0)

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun parse(clazz: Class<out kotlin.Enum<*>>, value: String?): SaksbehandlerValgEnum? =
                value?.let { java.lang.Enum.valueOf(clazz, it) as SaksbehandlerValgEnum }
        }
    }

    class Text @InternKonstruktoer constructor(override val defaultValue: String?, override val displayText: String) : SaksbehandlervalgVerdi<String?> {
        override val type = Type.TEXT
        override fun toString() = "SaksbehandlervalgVerdi.Text(text=$defaultValue)"
        override fun equals(other: Any?): Boolean {
            if (other !is Text) return false
            return defaultValue == other.defaultValue
        }

        override fun hashCode() = Text::class.java.hashCode() + (defaultValue?.hashCode() ?: 0)
    }
}