package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

object Constants {
    const val NAV_URL = "nav.no"
    const val ALDERSPENSJON_URL = "$NAV_URL/alderspensjon"
    const val DIN_PENSJON_URL = "$NAV_URL/din-pensjon"
    const val FULLMAKT_URL = "$NAV_URL/fullmakt"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val PENSJON_URL = "$NAV_URL/pensjon"
    const val SEND_BESKJED_URL = "$NAV_URL/send-beskjed"
    const val NAV_KONTAKTSENTER_AAPNINGSTID = "09.00-15.00"
    const val NAV_KONTAKTSENTER_OPEN_HOURS = "09:00-15:00"
    private val navKontaktsenterPensjon = Telefonnummer("55553334")
    val NAV_KONTAKTSENTER_TELEFON_PENSJON = navKontaktsenterPensjon.format()
    const val FAMILIEPLEIER_URL = "$NAV_URL/tidligere-familiepleier"
    const val NAV_KONTAKTSENTER_TELEFON = "55 55 33 33"
    const val GJENLEVENDEPENSJON_URL = "$NAV_URL/gjenlevendepensjon"
    const val UFOERETRYGD_URL = "$NAV_URL/uforetrygd"
    const val OMSORGSOPPTJENING_URL = "$NAV_URL/omsorgsopptjening"
    const val AFP_OFFENTLIG_URL = "$NAV_URL/afp-offentlig"
    const val ALDERSPENSJON_GJENLEVENDE_URL = "$NAV_URL/alderspensjon-gjenlevende"
    const val FORSOERGINGSTILLEGG_URL = "$NAV_URL/forsorgingstillegg"
    const val PERSONVERNERKLAERING_URL = "$NAV_URL/personvernerklaering"
    const val KONTAKT_ENG_URL = "$NAV_URL/contact"
    const val ALDERSPENSJON_GJENLEVENDE_URL_EN = "$ALDERSPENSJON_GJENLEVENDE_URL/en"
    const val UTBETALINGSOVERSIKT_URL = "$NAV_URL/utbetalingsoversikt"
    const val PENSJON_ENDRING_URL = "$NAV_URL/pensjon-endring"
    const val REGULERING_ALDERSPENSJON_URL = "$NAV_URL/reguleringalderspensjon"
    const val OMSTILLINGSSTOENAD_URL = "$NAV_URL/omstillingsstonad"


    // todo slett ubrukte
    const val ALDERSPENSJON = "$NAV_URL/alderspensjon"
    const val BESKJED_TIL_NAV_URL = "$NAV_URL/beskjedtilnav"
    const val DIN_UFOERETRYGD_URL = "$NAV_URL/dinuføretrygd"
    const val DIN_PENSJON_URL_INNLOGGET = "$NAV_URL/din-pensjon"
    const val DITT_NAV = "$NAV_URL/dittnav"
    const val ETTEROPPGJOR_URL = "$NAV_URL/etteroppgjor"
    const val ETTERSENDELSE_URL = "$NAV_URL/ettersendelse"
    const val HELFO = "HELFO"
    const val HELSENORGE = "helsenorge.no"
    const val HELSE_URL= "$NAV_URL/helse"
    const val INNTEKTSPLANLEGGEREN_URL = "$NAV_URL/inntektsplanleggeren"
    const val MEDLEMSKAP_URL = "$NAV_URL/medlemskap"
    const val MINSIDE_URL = "$NAV_URL/minside"
    const val GRUNNBELOEP_URL = "$NAV_URL/grunnbelop"
    const val SKATTEETATEN_URL = "skatteetaten.no"
    const val SKATTEETATEN_PENSJONIST_URL = "$SKATTEETATEN_URL/pensjonist"
    const val SKJEMA_URL = "$NAV_URL/skjema"
    const val SKRIV_TIL_OSS_URL = "$NAV_URL/skriv-til-oss"
    const val SUPPLERENDE_STOENAD_URL = "$NAV_URL/supplerende"
    const val TELEFON_HELSE = "800 HELSE (800 43 573)"
    const val UFOERETRYGD_ENDRING_URL = "$NAV_URL/uforetrygd-endring"
    const val MELDE_URL = "$UFOERETRYGD_URL#melde"
    const val UFOERE_ETTERSENDE_POST_URL = "$NAV_URL/uføre-ettersende-post"
    const val UFOERE_ETTERSENDE_URL = "$NAV_URL/uføre-ettersende"
    const val UFOERE_JOBB_URL = "$NAV_URL/uføre-jobb"
    const val UTBETALINGER_URL = "$NAV_URL/utbetalinger"
    const val SAKSBEHANDLINGSTID_URL = "$NAV_URL/saksbehandlingstider"
}