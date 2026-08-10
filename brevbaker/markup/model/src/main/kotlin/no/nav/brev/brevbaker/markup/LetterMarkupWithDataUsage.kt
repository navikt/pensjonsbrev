package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage.Property


/**
 * Et [LetterMarkup] beriket med metadata: hvilke datafelter brevet bruker ([letterDataUsage]) og
 * hvilken [Markup.Brevtype] det er. Beregnet på interne konsumenter (brevbaker/skribenten).
 */
@ConsistentCopyVisibility
data class LetterMarkupWithDataUsage internal constructor(
    val markup: LetterMarkup,
    val letterDataUsage: Set<Property>,
    val brevtype: Markup.Brevtype,
) {
    /** Et enkelt datafelt (type og property) brevet leser fra. */
    @ConsistentCopyVisibility
    data class Property internal constructor(
        val typeName: String,
        val propertyName: String,
    )
}
