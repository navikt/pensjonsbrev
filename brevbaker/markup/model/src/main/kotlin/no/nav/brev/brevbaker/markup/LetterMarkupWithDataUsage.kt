package no.nav.brev.brevbaker.markup

import java.util.Objects


/**
 * Et [LetterMarkup] beriket med metadata: hvilke datafelter brevet bruker ([letterDataUsage]) og
 * hvilken [Markup.Brevtype] det er. Beregnet på interne konsumenter (brevbaker/skribenten).
 */
class LetterMarkupWithDataUsage internal constructor(
    val markup: LetterMarkup,
    val letterDataUsage: Set<Property>,
    val brevtype: Markup.Brevtype,
) {
    override fun equals(other: Any?) = other is LetterMarkupWithDataUsage &&
            markup == other.markup &&
            letterDataUsage == other.letterDataUsage &&
            brevtype == other.brevtype

    override fun hashCode() = Objects.hash(markup, letterDataUsage, brevtype)
    override fun toString(): String = "Letter(letterDataUsage=$letterDataUsage, brevtype=$brevtype)"

    /** Et enkelt datafelt (type og property) brevet leser fra. */
    class Property internal constructor(
        val typeName: String,
        val propertyName: String,
    ) {
        override fun equals(other: Any?) = other is Property &&
                typeName == other.typeName &&
                propertyName == other.propertyName

        override fun hashCode() = Objects.hash(typeName, propertyName)
        override fun toString(): String = "Property(typeName=$typeName, propertyName=$propertyName)"
    }
}
