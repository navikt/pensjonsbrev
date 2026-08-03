package no.nav.brev.brevbaker.markup.dsl.extended

/**
 * Markerer den utvidede markup-DSL-en: eksplisitte id-er på hvert element, `variable(...)` og
 * `editBehaviour`.
 *
 * Den utvidede DSL-en bor bevisst i samme modul som den vanlige. Hadde den ligget i en egen modul,
 * måtte hver eneste builder-søm (`texts`, `blocks`, `contentFactory`, `build()`, `setTitle`) blitt
 * public for å krysse modulgrensen — altså måtte vi eksponert nøyaktig det maskineriet DSL-en finnes
 * for å skjule. Med én modul forblir sømmene `internal`, og skillet håndheves her i stedet.
 *
 * Skriver du en brevmal, er dette ikke flaten din: bruk den vanlige DSL-en, som genererer id-er selv.
 * Denne er for lag som selv eier id-tildelingen — i praksis `Letter2Markup` i `brevbaker:core`.
 */
@RequiresOptIn(
    message = "Utvidet markup-DSL: krever at kalleren selv tildeler id-er til hvert element. Bruk den vanlige DSL-en med mindre du eier id-tildelingen.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class ExtendedMarkupDsl
