package no.nav.pensjon.brevbaker.api.model

/**
 * Visningstekst for et felt i brevdata-modellen. Blir plukket opp av
 * `TemplateModelSpecification`-generatoren i brevbaker og vist i skribenten.
 *
 * Ligger her – og ikke sammen med `TemplateModelSpecification` i `brevbaker:internal` – fordi de
 * publiserte `*:api-model`-modulene annoterer sine Dto-felter med den.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class DisplayText(val text: String)
