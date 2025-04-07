@file:Suppress("unused")

package no.nav.pensjon.brev.api.model

// Brukerens egen sivilstand tolket ut fra om beregningsresultat fra PREG
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

enum class YtelseForAldersovergangKode {
    FAM_PL,
    GJP_AVKORT,
    GJP_FULL,
    INGEN_YT,
    UT,
    UT_AP_GRAD,
    UT_GRAD,
}

enum class AlderspensjonRegelverkType {
    AP1967,
    AP2011,
    AP2016,
    AP2025,
}

enum class AlderspensjonBeregnetEtter {
    EGEN,
    AVDOD,
}

// Brukes kun for konverterte brev. For nytt innhold, bruk brukersSivilstand eller bormedSivilstand
enum class MetaforceSivilstand {
    EKTEFELLE, ENKE, ENSLIG,
    FELLES_BARN, FORELDER, GIFT, GLAD_EKT, GLAD_PART, PARTNER, SAMBOER,
    SAMBOER_1_5,
    SAMBOER_3_2, SEPARERT, SEPARERT_PARTNER, UKJENT;
}