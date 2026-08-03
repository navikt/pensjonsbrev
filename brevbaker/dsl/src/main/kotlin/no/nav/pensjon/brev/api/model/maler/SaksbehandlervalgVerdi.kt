package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

private val logger = LoggerFactory.getLogger(SaksbehandlervalgVerdi::class.java)

@BrevbakerDSLInternal
sealed interface SaksbehandlervalgVerdi<T> {
    val id: String
    val displayText: String
    val typename: String

    fun getValue(saksbehandlervalg: SaksbehandlervalgIDSL): T

    @BrevbakerDSLInternal
    data class Bool @InternKonstruktoer constructor(override val id: String, override val displayText: String) : SaksbehandlervalgVerdi<Boolean?> {
        override val typename: String = "Boolean"
        override fun getValue(saksbehandlervalg: SaksbehandlervalgIDSL): Boolean? = when {
            saksbehandlervalg[id] == null -> null
            saksbehandlervalg[id] is Boolean -> saksbehandlervalg[id] as Boolean
            else -> null.also { logger.error("Saksbehandlervalg med id $id er deklarert som Boolean, men typen er ikke det") }
        }
    }

    @BrevbakerDSLInternal
    data class Integer @InternKonstruktoer constructor(override val id: String, override val displayText: String) : SaksbehandlervalgVerdi<Int?> {
        override val typename: String = "Int"
        override fun getValue(saksbehandlervalg: SaksbehandlervalgIDSL): Int? = when {
            saksbehandlervalg[id] == null -> null
            saksbehandlervalg[id] is Int -> saksbehandlervalg[id] as Int
            (saksbehandlervalg[id] as? String)?.toIntOrNull() != null -> (saksbehandlervalg[id] as String).toIntOrNull()
            else -> null.also { logger.error("Saksbehandlervalg med id $id er deklarert som Int, men typen er ikke det") }
        }
    }

    @BrevbakerDSLInternal
    data class Enum<T> @InternKonstruktoer constructor(
        override val id: String,
        override val displayText: String,
        val clazz: KClass<T>,
    ) : SaksbehandlervalgVerdi<T?> where T : kotlin.Enum<T>, T : SaksbehandlerValgEnum {
        override val typename: String = clazz.qualifiedName!!
        override fun getValue(saksbehandlervalg: SaksbehandlervalgIDSL): T? = when {
            saksbehandlervalg[id] == null -> null
            saksbehandlervalg[id] is String -> (saksbehandlervalg[id] as String).let { java.lang.Enum.valueOf(clazz.java, it) }
            else -> null.also { logger.error("Saksbehandlervalg med id $id er deklarert som Enum, men typen er ikke det") }
        }
    }

    @BrevbakerDSLInternal
    data class Text @InternKonstruktoer constructor(
        override val id: String,
        override val displayText: String,
    ) : SaksbehandlervalgVerdi<String?> {
        override val typename: String = "String"
        override fun getValue(saksbehandlervalg: SaksbehandlervalgIDSL): String? = when {
            saksbehandlervalg[id] == null -> null
            saksbehandlervalg[id] is String -> saksbehandlervalg[id] as String
            else -> null.also { logger.error("Saksbehandlervalg med id $id er deklarert som String, men typen er ikke det") }
        }
    }
}