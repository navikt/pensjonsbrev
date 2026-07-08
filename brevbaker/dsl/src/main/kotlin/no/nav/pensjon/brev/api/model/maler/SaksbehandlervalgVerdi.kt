package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import kotlin.reflect.KClass

@BrevbakerDSLInternal
sealed interface SaksbehandlervalgVerdi<out T> {
    @BrevbakerDSLInternal
    enum class Type {
        BOOL, INTEGER, ENUM, TEXT
    }

    val id: String
    val defaultValue: T
    val type: Type
    val displayText: String

    val typename: String

    fun getValue(saksbehandlervalg: Map<String, *>): T

    @BrevbakerDSLInternal
    data class Bool @InternKonstruktoer constructor(override val id: String, override val defaultValue: Boolean, override val displayText: String) : SaksbehandlervalgVerdi<Boolean> {
        override val type = Type.BOOL
        override val typename: String = "Boolean"
        override fun getValue(saksbehandlervalg: Map<String, *>): Boolean = saksbehandlervalg[id] as? Boolean ?: defaultValue
    }

    @BrevbakerDSLInternal
    data class Integer @InternKonstruktoer constructor(override val id: String, override val defaultValue: Int?, override val displayText: String) : SaksbehandlervalgVerdi<Int?> {
        override val type = Type.INTEGER
        override val typename: String = "Int"
        override fun getValue(saksbehandlervalg: Map<String, *>): Int? = saksbehandlervalg[id] as? Int ?: defaultValue
    }

    @BrevbakerDSLInternal
    data class Enum<T> @InternKonstruktoer constructor(
        override val id: String,
        override val defaultValue: T?,
        override val displayText: String,
        val clazz: KClass<T>,
    ) : SaksbehandlervalgVerdi<T?> where T : kotlin.Enum<T>, T : SaksbehandlerValgEnum {
        override val type = Type.ENUM
        override val typename: String = clazz.qualifiedName!!
        override fun getValue(saksbehandlervalg: Map<String, *>): T? = (saksbehandlervalg[id] as? String)
            ?.let { java.lang.Enum.valueOf(clazz.java, it) }
            ?: defaultValue
    }

    @BrevbakerDSLInternal
    data class Text @InternKonstruktoer constructor(
        override val id: String,
        override val defaultValue: String?,
        override val displayText: String,
    ) : SaksbehandlervalgVerdi<String?> {
        override val type = Type.TEXT
        override val typename: String = "String"
        override fun getValue(saksbehandlervalg: Map<String, *>): String? = saksbehandlervalg[id] as? String ?: defaultValue
    }
}