package no.nav.pensjon.brev.alder.model

import no.nav.pensjon.brevbaker.api.model.DisplayText

enum class AlderspensjonRegelverkType {
    AP1967,
    AP2011,
    AP2016,
    AP2025,
}

enum class Sivilstand {
    ENSLIG,
    ENKE,
    GIFT,
    SEPARERT,
    PARTNER,
    SEPARERT_PARTNER,
}

enum class SivilstandAvdoed {
    GIFT,
    SAMBOER1_5,
    SAMBOER3_2,
    PARTNER,
}

// Bruker er koblet mot en ektefelle, partner eller samboer og angir bor med/benyttet sivilstatus i beregningen
enum class BorMedSivilstand{
    PARTNER_LEVER_ADSKILT,
    GIFT_LEVER_ADSKILT,
    EKTEFELLE,
    PARTNER,
    SAMBOER1_5,
    SAMBOER3_2,
}

enum class Institusjon {
    FENGSEL,
    HELSE,
    SYKEHJEM,
    INGEN,
}

enum class MetaforceSivilstand {
    EKTEFELLE, ENKE, ENSLIG,
    FELLES_BARN, FORELDER, GIFT, GLAD_EKT, GLAD_PART, PARTNER, SAMBOER,
    SAMBOER_1_5,
    SAMBOER_3_2, SEPARERT, SEPARERT_PARTNER, UKJENT
}

enum class KravArsakType{
    ALDERSOVERGANG,
    ANNET, // Denne er catch-all for alle de andre typene som fins i pesys, men som ikke trengs her
    ENDRET_OPPTJENING,
    EPS_ENDRET_INNTEKT,
    EPS_NY_YTELSE,
    EPS_NY_YTELSE_UT,
    EPS_OPPH_YTELSE_UT,
    INNVANDRET,
    INSTOPPHOLD,
    SIVILSTANDSENDRING,
    TILSTOT_ENDR_YTELSE,
    TILSTOT_OPPHORT,
    TILSTOT_DOD,
    UTTAKSGRAD,
    UTVANDRET,
    VURDER_SERSKILT_SATS
}

enum class AlderspensjonBeregnetEtter {
    EGEN,
    AVDOD,
}

enum class BeloepEndring {
    @DisplayText("Økning av beløpet")
    ENDR_OKT,
    @DisplayText("Reduksjon av beløpet")
    ENDR_RED,
    @DisplayText("Uendret beløp")
    UENDRET
}

enum class GarantipensjonSatsType {
    HOY, ORDINAER
}

enum class Beregningsmetode {
    AUSTRALIA,
    CANADA,
    CHILE,
    EOS,
    FOLKETRYGD,
    INDIA,
    ISRAEL,
    NORDISK,
    PRORATA,
    SOR_KOREA,
    SVEITS,
    USA
}

enum class PoengTallsType { FPP, G, H, IFUT, J, K, L, PI }

enum class InformasjonOmMedlemskap {
    EOES,
    UTENFOR_EOES,
    IKKE_RELEVANT
}
enum class EksportForbudKode {
    FLYKT_ALDER,
    TPUNGUFOR_ALDER,
    UFOR25_ALDER,
    DOD26_ALDER
}

enum class YtelseForAldersovergangKode {
    FAM_PL,
    GJP_AVKORT,
    GJP_FULL,
    INGEN_YT,
    UT,
    UT_AP_GRAD,
    UT_GRAD,
}