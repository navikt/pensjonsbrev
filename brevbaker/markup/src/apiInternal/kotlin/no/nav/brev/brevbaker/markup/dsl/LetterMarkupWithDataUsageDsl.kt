package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Brevtype
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage

/**
 * Lag en [LetterMarkupWithDataUsage.Property] – ett datafelt (type + property) brevet leser fra.
 *
 * Parametere:
 * - `typeName`: navnet på datatypen (for eksempel `UngUfoerDto`)
 * - `propertyName`: navnet på feltet i typen
 *
 * ```
 * val prop = dataUsageProperty(typeName = "UngUfoerDto", propertyName = "totaltUfoerePerMnd")
 * ```
 */
fun dataUsageProperty(typeName: String, propertyName: String): LetterMarkupWithDataUsage.Property =
    LetterMarkupWithDataUsage.Property(typeName, propertyName)

/**
 * Bygg et [LetterMarkupWithDataUsage] – et [LetterMarkup] beriket med [brevtype] og hvilke datafelter
 * brevet bruker ([letterDataUsage]).
 *
 * Parametere:
 * - `brevtype`: hvilken brevtype markuppen tilhører
 * - `letterDataUsage`: sett av datafelter brevet bruker (kan stå tomt)
 * - `build`: selve brevinnholdet
 *
 * ```
 * val brev = letterMarkupWithDataUsage(
 *     brevtype = Brevtype.VEDTAKSBREV,
 *     letterDataUsage = setOf(dataUsageProperty("UngUfoerDto", "belop")),
 * ) {
 *     saksinformasjon(/* ... */); outline { paragraph("...") }; signatur(/* ... */)
 * }
 * ```
 */
fun letterMarkupWithDataUsage(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = letterMarkup(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)

/**
 * Som [letterMarkupWithDataUsage], men med støtte for elementer som ikke brukes til rendring av pdf.
 *
 * Parametere:
 * - `brevtype`: hvilken brevtype markuppen tilhører
 * - `letterDataUsage`: sett av datafelter brevet bruker (kan stå tomt)
 * - `build`: selve brevinnholdet
 *
 * ```
 * val brev = letterMarkupWithDataUsageExtended(brevtype = Brevtype.INFORMASJONSBREV) {
 *     outline { paragraph { text("Beløp: "); variable("1000 kr") } }
 * }
 * ```
 */
fun letterMarkupWithDataUsageExtended(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = letterMarkupExtended(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)
