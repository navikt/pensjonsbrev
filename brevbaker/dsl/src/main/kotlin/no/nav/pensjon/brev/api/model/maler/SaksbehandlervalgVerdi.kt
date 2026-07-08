package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import kotlin.reflect.KClass

@BrevbakerDSLInternal
sealed interface SaksbehandlervalgVerdi<out T> {
    val id: String
    val displayText: String
    val typename: String

    fun getValue(saksbehandlervalg: Map<String, *>): T

    @BrevbakerDSLInternal
    data class Bool @InternKonstruktoer constructor(override val id: String, override val displayText: String) : SaksbehandlervalgVerdi<Boolean?> {
        override val typename: String = "Boolean"
        override fun getValue(saksbehandlervalg: Map<String, *>): Boolean? = saksbehandlervalg[id] as? Boolean
    }

    @BrevbakerDSLInternal
    data class Integer @InternKonstruktoer constructor(override val id: String, override val displayText: String) : SaksbehandlervalgVerdi<Int?> {
        override val typename: String = "Int"
        override fun getValue(saksbehandlervalg: Map<String, *>): Int? = (saksbehandlervalg[id] as? Int) ?: ((saksbehandlervalg[id] as? String)?.toIntOrNull())
    }

    @BrevbakerDSLInternal
    data class WithDefault<T : Any> @InternKonstruktoer constructor(val saksbehandlervalgVerdi: SaksbehandlervalgVerdi<T?>, val defaultValue: T) : SaksbehandlervalgVerdi<T> {
        override val id: String
            get() = saksbehandlervalgVerdi.id
        override val displayText: String
            get() = saksbehandlervalgVerdi.displayText
        override val typename: String
            get() = saksbehandlervalgVerdi.typename
        override fun getValue(saksbehandlervalg: Map<String, *>): T = saksbehandlervalgVerdi.getValue(saksbehandlervalg) ?: defaultValue
    }

    @BrevbakerDSLInternal
    data class Enum<T> @InternKonstruktoer constructor(
        override val id: String,
        override val displayText: String,
        val clazz: KClass<T>,
    ) : SaksbehandlervalgVerdi<T?> where T : kotlin.Enum<T>, T : SaksbehandlerValgEnum {
        override val typename: String = clazz.qualifiedName!!
        override fun getValue(saksbehandlervalg: Map<String, *>): T? = (saksbehandlervalg[id] as? String)
            ?.let { java.lang.Enum.valueOf(clazz.java, it) }
    }

    @BrevbakerDSLInternal
    data class Text @InternKonstruktoer constructor(
        override val id: String,
        override val displayText: String,
    ) : SaksbehandlervalgVerdi<String?> {
        override val typename: String = "String"
        override fun getValue(saksbehandlervalg: Map<String, *>): String? = saksbehandlervalg[id] as? String
    }
}