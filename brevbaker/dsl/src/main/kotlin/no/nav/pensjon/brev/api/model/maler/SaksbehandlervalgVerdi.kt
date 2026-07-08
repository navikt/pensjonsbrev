package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.BrevbakerDSLInternal

@BrevbakerDSLInternal
sealed interface SaksbehandlervalgVerdi<out T> {
    @BrevbakerDSLInternal
    enum class Type {
        BOOL, INTEGER, ENUM, TEXT
    }

    val defaultValue: T
    val type: Type
    val displayText: String

    @BrevbakerDSLInternal
    data class Bool @InternKonstruktoer constructor(override val defaultValue: Boolean, override val displayText: String) : SaksbehandlervalgVerdi<Boolean> {
        override val type = Type.BOOL
    }

    @BrevbakerDSLInternal
    data class Integer @InternKonstruktoer constructor(override val defaultValue: Int?, override val displayText: String) : SaksbehandlervalgVerdi<Int?> {
        override val type = Type.INTEGER
    }

    @BrevbakerDSLInternal
    data class Enum<T : SaksbehandlerValgEnum> @InternKonstruktoer constructor(override val defaultValue: T?, override val displayText: String, val clazz: Class<out kotlin.Enum<*>?>) : SaksbehandlervalgVerdi<T?> {
        override val type = Type.ENUM
    }

    @BrevbakerDSLInternal
    data class Text @InternKonstruktoer constructor(override val defaultValue: String?, override val displayText: String) : SaksbehandlervalgVerdi<String?> {
        override val type = Type.TEXT
    }
}