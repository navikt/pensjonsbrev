package no.nav.brev.brevbaker.markup

/**
 * Markerer konstruksjons-seams i markup som kun er ment for intern bruk mellom brevbaker,
 * skribenten og pdf-bygger — typisk konstruktører og builder-internals som den utvidede
 * (id-eksplisitte) DSL-en i `brevbaker:internal` trenger.
 *
 * Disse var tidligere `internal` og ble nådd via Kotlin friend-compilation fra kildesettet
 * `markup/src/apiInternal`. Etter at det kildesettet ble flyttet ut i modulen `brevbaker:internal`,
 * som konsumerer markup som et *publisert* artefakt, er friend-compilation ikke lenger mulig.
 * Seamsene er derfor `public`, men opt-in — og filtrert bort fra ABI-validering, på samme måte som
 * `@InternKonstruktoer` i api-model-common.
 *
 * Ekstern bruk er ikke støttet: bygg brev med den offentlige DSL-en i `no.nav.brev.brevbaker.markup.dsl`.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "Dette er en intern konstruksjons-seam i markup. Bruk den offentlige DSL-en i no.nav.brev.brevbaker.markup.dsl.",
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
annotation class MarkupInternalApi
