package no.nav.brev.brevbaker.markup

/**
 * Markerer flaten som konstruerer markup-modellen direkte, utenom DSL-en.
 *
 * Modellens konstruktører er `internal` (+ `@ConsistentCopyVisibility`), slik at verken `copy()` eller
 * konstruktørene er tilgjengelige utenfor denne modulen. [MarkupModel] er den eneste veien inn utenfra,
 * og den er ment for `brevbaker:markup:dsl` og `brevbaker:core` — ikke for malforfattere.
 * Bygger du et brev, bruk DSL-en: den validerer strukturen (tabellbredder, ikke-tomme valg osv.), og
 * den valideringen kan ikke omgås så lenge du går gjennom den.
 */
@RequiresOptIn(
    message = "Konstruerer markup-modellen direkte og hopper over valideringen i DSL-en. Bruk markup-DSL-en med mindre du implementerer et DSL-lag selv.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class MarkupModelApi
