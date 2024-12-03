package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

object Constants {
    const val NAV_URL = "nav.no"

    const val ALDERSPENSJON_GJENLEVENDE_URL = "$NAV_URL/alderspensjon-gjenlevende"
    const val AFP_OFFENTLIG_URL = "$NAV_URL/afp-offentlig"
    const val ARBEID_URL= "$NAV_URL/arbeid"
    const val BESKJED_TIL_NAV_URL = "$NAV_URL/beskjedtilnav"
    const val DIN_UFOERETRYGD_URL = "$NAV_URL/dinuføretrygd"
    const val PENSJON_URL = "$NAV_URL/pensjon"
    const val DITT_NAV = "$NAV_URL/dittnav"
    const val ETTEROPPGJOR_URL = "$NAV_URL/etteroppgjor"
    const val ETTERSENDELSE_URL = "$NAV_URL/ettersendelse"
    const val FORSOERGINGSTILLEGG_URL = "$NAV_URL/forsorgingstillegg"
    const val FULLMAKT_URL = "$NAV_URL/fullmakt"
    const val GJENLEVENDEPENSJON_URL = "$NAV_URL/gjenlevendepensjon"
    const val GJENLEVENDETILLEGG_URL = "$NAV_URL/gjenlevendetillegg"
    const val GJENLEVENDE_SKJEMA_URL = "$NAV_URL/gjenlevendeektefelle"
    const val GJENLEVENDE_TILLEGGSSTOENADER_URL= "$NAV_URL/tilleggsstonader-gjenlevende"
    const val HELSE_URL= "$NAV_URL/helse"
    const val INNTEKTSPLANLEGGEREN_URL = "$NAV_URL/inntektsplanleggeren"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    const val KONTAKT_ENG_URL = "$NAV_URL/contact"
    const val NAV_KONTAKTSENTER_AAPNINGSTID = "09:00-15:00"
    const val NAV_KONTAKTSENTER_TELEFON = "55 55 33 33"
    val navKontaktsenterPensjon = Telefonnummer("55553334")
    val NAV_KONTAKTSENTER_TELEFON_PENSJON = navKontaktsenterPensjon.format()
    const val OMSORGSOPPTJENING_URL = "$NAV_URL/omsorgsopptjening"
    const val PENSJON_ENDRING_URL = "$NAV_URL/pensjon-endring"
    const val GRUNNBELOEP_URL = "$NAV_URL/grunnbelop"
    const val REGULERING_ALDERSPENSJON_URL = "$NAV_URL/reguleringalderspensjon"
    const val SEND_BESKJED_URL = "$NAV_URL/send-beskjed"
    const val SKATTEETATEN_URL = "skatteetaten.no"
    const val SKJEMA_URL = "$NAV_URL/skjema"
    const val SKRIV_TIL_OSS_URL = "$NAV_URL/skriv-til-oss"
    const val UFOERETRYGD_ENDRING_URL = "$NAV_URL/uforetrygd-endring"
    const val UFOERETRYGD_URL = "$NAV_URL/uforetrygd"
    const val UFOERE_ETTERSENDE_POST_URL = "$NAV_URL/uføre-ettersende-post"
    const val UFOERE_ETTERSENDE_URL = "$NAV_URL/uføre-ettersende"
    const val UTBETALINGSOVERSIKT_URL = "$NAV_URL/utbetalingsoversikt"
    const val SAKSBEHANDLINGSTID_URL = "$NAV_URL/saksbehandlingstider"
}