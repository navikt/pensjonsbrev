package no.nav.pensjon.etterlatte.maler.fraser.common

object Constants {
    const val NAV_URL = "nav.no"
    const val SAKSBEHANDLINGSTIDER_URL = "$NAV_URL/saksbehandlingstider"
    const val BARNEPENSJON_URL = "$NAV_URL/barnepensjon"
    const val DIN_PENSJON_URL = "$NAV_URL}/dinpensjon"
    const val FULLMAKT_URL = "$NAV_URL/fullmakt"
    const val UTBETALING_URL = "$NAV_URL/utbetalinger"
    const val UTBETALINGSDATOER_URL = "$NAV_URL/utbetalingsdatoer"
    const val SKRIVTILOSS_URL = "$NAV_URL/skrivtiloss"
    const val KONTONUMMER_URL = "$NAV_URL/kontonummer"
    const val ENDRING_KONTONUMMER_URL = "$NAV_URL/start/soknad-endring-bankkontonummer"
    const val KONTAKT_URL = "$NAV_URL/kontaktoss"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val GRUNNBELOEP_URL = "$NAV_URL/grunnbelopet"
    const val ETTERSENDELSE_URL = "$NAV_URL/ettersendelse"
    const val BESKJED_TIL_NAV_URL = "$NAV_URL/beskjedtilnav"
    const val SKATTETREKK_ETTERBETALING_URL = "$NAV_URL/skattetrekk#etterbetaling"
    const val SKATTETREKK_KILDESKATT_URL = "$NAV_URL/skattetrekk#kildeskatt"
    const val OMS_ETTEROPPGJOER_URL = "$NAV_URL/omstillingsstønad#etteroppgjør"
    const val OMS_HVORLENGE_URL = "$NAV_URL/omstillingsstønad#hvor-lenge"
    const val OMS_AKTIVITET_URL = "$NAV_URL/omstillingsstønad#aktivitet"
    const val OMS_URL = "$NAV_URL/omstillingsstønad"
    const val POSTADRESSE = "NAV skanning, Postboks 1400, 0109 OSLO"
    const val KONTAKTTELEFON_PENSJON = "55 55 33 34"
    const val KONTATTELEFON_PENSJON_MED_LANDKODE = "+ 47 $KONTAKTTELEFON_PENSJON"
    const val KONTAKTTELEFON_GENERELL = "55 55 33 33"
    const val KONTAKTTELEFON_SKATT = "800 80 000"
    const val SAKSBEHANDLINGSTIDER_BP = "$SAKSBEHANDLINGSTIDER_URL#barnepensjon"
    const val SAKSBEHANDLINGSTIDER_OMS = "$SAKSBEHANDLINGSTIDER_URL#omstillingsstonad"

    object Engelsk {
        const val UTBETALINGSDATOER_URL = "${Constants.UTBETALINGSDATOER_URL}/en"
        const val KLAGE_URL = "${Constants.KLAGE_URL}/en"
        const val BARNEPENSJON_URL = "${Constants.BARNEPENSJON_URL}/en"
        const val SKRIVTILOSS_URL = "${Constants.SKRIVTILOSS_URL}/en"
        const val KONTONUMMER_URL = "${Constants.KONTONUMMER_URL}/en"
        const val FULLMAKT_URL = "${Constants.FULLMAKT_URL}/en"
        const val KONTAKT_URL = "${Constants.KONTAKT_URL}/en"
    }

    object Utland {
        const val UTBETALING_INFO = "${NAV_URL}/utbetaling-utland"
        const val ENDRE_KONTONUMMER_SKJEMA_URL = "${NAV_URL}/soknader/en#bank-account-number-how-to-change"
        const val KONTAKTTELEFON_SKATT = "+47 22 07 70 00"
    }
}
