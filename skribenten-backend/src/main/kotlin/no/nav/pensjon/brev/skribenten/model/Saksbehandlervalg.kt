package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.pensjon.brev.skribenten.serialize.SaksbehandlervalgVerdiDeserializer

typealias SaksbehandlervalgMap = Api.GeneriskSaksbehandlervalg<String, Any?>

/**
 * The type used specifically for saksbehandlerValg-values created/edited directly by a saksbehandler in
 * Skribenten (via [Api.OpprettBrevRequest], [Api.OppdaterBrevRequest], [Api.OppdaterAttesteringRequest]).
 *
 * This is intentionally narrower than [SaksbehandlervalgMap]: letters can also be created via the external
 * API by third-party systems with richer, nested saksbehandlerValg-structures (see [no.nav.pensjon.brev.skribenten.eksterntApi.ExternalAPI.OpprettBrevRequest]).
 * Those values are never edited in Skribenten. When updating an existing brev, Skribenten only ever
 * submits the subset of fields it actually renders an editor for, and the resulting [RedigerbarSaksbehandlervalgMap]
 * is merged into the existing, unconstrained [SaksbehandlervalgMap] (see [mergeInn]) rather
 * than replacing it wholesale - that way the non-editable, richer values survive unaffected.
 */
typealias RedigerbarSaksbehandlervalgMap = Api.GeneriskSaksbehandlervalg<String, SaksbehandlervalgVerdi?>

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

/** Converts saksbehandler-provided input to the unconstrained, storage/brevbaker-facing [SaksbehandlervalgMap]. */
fun RedigerbarSaksbehandlervalgMap.toSaksbehandlerValg(): SaksbehandlervalgMap =
    SaksbehandlervalgMap(mapValues { (_, verdi) -> verdi?.value }.toList())

/**
 * Merges saksbehandler-provided [input] on top of this (already stored) [SaksbehandlervalgMap], overwriting
 * only the keys present in [input] and leaving everything else - notably any richer, non-editable values
 * originating from the external API - untouched.
 */
fun SaksbehandlervalgMap.mergeInn(input: RedigerbarSaksbehandlervalgMap): SaksbehandlervalgMap =
    SaksbehandlervalgMap().also { result ->
        result.putAll(this)
        result.putAll(input.toSaksbehandlerValg())
    }

/**
 * Converts this (already stored) [SaksbehandlervalgMap] to a [RedigerbarSaksbehandlervalgMap], for use in
 * [Api.BrevResponse.saksbehandlerValg]. Values that don't fit the strict [SaksbehandlervalgVerdi] scalar
 * union are silently dropped rather than converted: a brev created via the external API may legitimately
 * hold richer, nested saksbehandlerValg-values, but those are never rendered/edited by Skribenten (they're
 * only relevant when merging saksbehandler-provided edits back in, see [mergeInn]), so
 * there's no need for the response to carry them. Doing this lets the generated OpenAPI/TypeScript type for
 * [Api.BrevResponse.saksbehandlerValg] match the strict scalar type Skribenten's own editors expect, instead
 * of `unknown`.
 */
fun SaksbehandlervalgMap.toRedigerbarSaksbehandlervalgMap(): RedigerbarSaksbehandlervalgMap =
    mapNotNull { (key, value) ->
        when (value) {
            null -> key to null
            is Boolean -> key to SaksbehandlervalgVerdi.Boolean(value)
            is Int -> key to SaksbehandlervalgVerdi.Int(value)
            is String -> key to SaksbehandlervalgVerdi.String(value)
            else -> null
        }
    }.let { RedigerbarSaksbehandlervalgMap(it) }