package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage
import no.nav.brev.brevbaker.markup.Markup

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
 * Bygg et [LetterMarkupWithDataUsage] – en ferdig [LetterMarkup] beriket med [brevtype] og hvilke
 * datafelter brevet bruker ([letterDataUsage]).
 *
 * Parametere:
 * - `markup`: den ferdige brevmarkuppen
 * - `brevtype`: hvilken brevtype markuppen tilhører
 * - `letterDataUsage`: sett av datafelter brevet bruker (kan stå tomt)
 *
 * ```
 * val brev = letterMarkupWithDataUsage(
 *     markup = letterMarkup(saksinformasjon = saksinformasjon(...), signatur = signatur(...)) {
 *         outline { paragraph("...") }
 *     },
 *     brevtype = Markup.Brevtype.VEDTAKSBREV,
 *     letterDataUsage = setOf(dataUsageProperty("UngUfoerDto", "belop")),
 * )
 * ```
 */
fun letterMarkupWithDataUsage(
    markup: LetterMarkup,
    brevtype: Markup.Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = markup,
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)
