package no.nav.pensjon.etterlatte.maler.fraser.common

object Constants {
    const val NAV_URL = "nav.no"
    const val BARNEPENSJON_URL = "$NAV_URL/barnepensjon"
    const val FULLMAKT_URL = "$NAV_URL/fullmakt"
    const val UTBETALING_URL = "$NAV_URL/utbetalinger"
    const val UTBETALINGSDATOER_URL = "$NAV_URL/utbetalingsdatoer"
    const val SKRIVTILOSS_URL = "$NAV_URL/skrivtiloss"
    const val KONTONUMMER_URL = "$NAV_URL/kontonummer"
    const val ENDRING_KONTONUMMER_URL = "$NAV_URL/start/soknad-endring-bankkontonummer"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val KLAGERETTIGHETER_URL = "$NAV_URL/klagerettigheter"
    const val SKATTETREKK_PENGESTOETTE_URL = "$NAV_URL/skatt-pengestotte"
    const val SKATTETREKK_ETTERBETALING_URL = "$NAV_URL/skatt-etterbetaling"
    const val OMS_REGULERING_URL = "$NAV_URL/omstillingsstønad#regulering"
    const val OMS_ETTEROPPGJOER_URL = "$NAV_URL/omstillingsstønad#etteroppgjør"
    const val OMS_HVORMYE_URL = "$NAV_URL/omstillingsstønad#hvor-mye"
    const val OMS_URL = "$NAV_URL/omstillingsstønad"
    const val POSTADRESSE = "NAV Familie- og Pensjonsytelser, Postboks 6600 Etterstad, 0607 OSLO"
    const val KONTAKTTELEFON_PENSJON = "55 55 33 34"
    const val KONTAKTTELEFON_GENERELL = "55 55 33 33"

    object Engelsk {
        const val UTBETALINGSDATOER_URL = "${Constants.UTBETALINGSDATOER_URL}/en"
        const val KLAGE_URL = "${Constants.KLAGE_URL}/en"
        const val BARNEPENSJON_URL = "${Constants.BARNEPENSJON_URL}/en"
        const val SKRIVTILOSS_URL = "${Constants.SKRIVTILOSS_URL}/en"
        const val KONTONUMMER_URL = "${Constants.KONTONUMMER_URL}/en"
        const val FULLMAKT_URL = "${Constants.FULLMAKT_URL}/en"
    }
}