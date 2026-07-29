package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable


/**
 * Et [LetterMarkup] beriket med metadata: hvilke datafelter brevet bruker ([letterDataUsage]) og
 * hvilken [Markup.Brevtype] det er. Beregnet på interne konsumenter (brevbaker/skribenten).
 */
@Serializable
data class LetterMarkupWithDataUsage(
    val markup: LetterMarkup,
    val letterDataUsage: Set<Property>,
    val brevtype: Markup.Brevtype,
) {
    /** Et enkelt datafelt (type og property) brevet leser fra. */
    @Serializable
    data class Property(
        val typeName: String,
        val propertyName: String,
    )
}
