package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.pensjon.brev.skribenten.serialize.SaksbehandlervalgVerdiDeserializer

class SaksbehandlervalgMap(entries: Iterable<Pair<String, SaksbehandlervalgVerdi?>>? = null) :
    LinkedHashMap<String, SaksbehandlervalgVerdi?>() {
    init {
        if (entries != null) putAll(entries)
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.NOTHING)
@JsonSubTypes(
    JsonSubTypes.Type(SaksbehandlervalgVerdi.Boolean::class),
    JsonSubTypes.Type(SaksbehandlervalgVerdi.Int::class),
    JsonSubTypes.Type(SaksbehandlervalgVerdi.String::class),
)
@JsonDeserialize(using = SaksbehandlervalgVerdiDeserializer::class)
sealed interface SaksbehandlervalgVerdi {
    val value: Any

    @JvmInline
    value class Boolean(override val value: kotlin.Boolean): SaksbehandlervalgVerdi
    @JvmInline
    value class Int(override val value: kotlin.Int): SaksbehandlervalgVerdi
    @JvmInline
    value class String(override val value: kotlin.String): SaksbehandlervalgVerdi
}

/**
 * Merges saksbehandler-provided [input] on top of this (already stored) [SaksbehandlervalgMap], overwriting
 * only the keys present in [input] and leaving everything else - notably any richer, non-editable values
 * originating from the external API - untouched.
 */
fun SaksbehandlervalgMap.mergeInn(input: SaksbehandlervalgMap): SaksbehandlervalgMap =
    SaksbehandlervalgMap().also { result ->
        result.putAll(this)
        result.putAll(input)
    }