package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable

/**
 * Et [LetterMarkup] beriket med metadata: hvilke datafelter brevet bruker ([letterDataUsage]) og
 * hvilken [Markup.Brevtype] det er. Beregnet på interne konsumenter (brevbaker/skribenten).
 */
@ConsistentCopyVisibility
@Serializable
data class LetterMarkupWithDataUsage internal constructor(
    val markup: LetterMarkup,
    val letterDataUsage: Set<Property>,
    val brevtype: Markup.Brevtype,
) {
    /** Et enkelt datafelt (type og property) brevet leser fra. */
    @ConsistentCopyVisibility
    @Serializable
    data class Property internal constructor(
        val typeName: String,
        val propertyName: String,
    )
}

/** Serialiser et [LetterMarkupWithDataUsage] til JSON. */
fun LetterMarkupWithDataUsage.toJson(): String =
    markupJson.encodeToString(LetterMarkupWithDataUsage.serializer(), this)

/** Deserialiser et [LetterMarkupWithDataUsage] fra JSON. */
fun decodeLetterMarkupWithDataUsage(json: String): LetterMarkupWithDataUsage =
    markupJson.decodeFromString(LetterMarkupWithDataUsage.serializer(), json)
