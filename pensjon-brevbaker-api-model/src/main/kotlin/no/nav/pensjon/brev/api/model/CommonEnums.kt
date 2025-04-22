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

enum class AlderspensjonRegelverkstype {
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

enum class PoengTallsType { FPP, G, H, IFUT, J, K, L, PI }

enum class TilbakekrevingResultat  {
    DELVIS_TILBAKEKREV,
    FEILREGISTRERT,
    FORELDET,
    FULL_TILBAKEKREV,
    INGEN_TILBAKEKREV,
}

enum class KonteringType {
    AAP,
    AFP_KOMP_TILLEGG,
    AFP_KRONETILLEGG,
    AFP_LIVSVARIG,
    AFP_T,
    ANNET,
    AP_GJT,
    BARNETILSYN,
    BT,
    ET,
    FAM_T,
    FAST_UTGIFT_T,
    FEILKONTO,
    GAP,
    GARANTITILLEGG,
    GAT,
    AP_GJT_KAP19,
    GP,
    HJELP_BIDRAG,
    HJELP_I_HUS,
    IP,
    JUSTERINGSKONTO,
    KRIG_GY,
    MENDEL,
    MIN_NIVA_TILL_INDV,
    MIN_NIVA_TILL_PPAR,
    MISK,
    PT,
    SISK,
    SKATT,
    SKATT_F_GP,
    SKATT_F_T,
    SKATT_F_UT_ORDINER,
    SKATT_PALEGG,
    SKJERMT,
    SP,
    ST,
    TFB,
    TILSKOTT_FLYTTEUTG,
    TJENESTEPENSJON,
    TP,
    TREKK,
    TSB,
    UFORETILLEGG_AP,
    UTD_STONAD,
    UT_AAP,
    UT_ET,
    UT_FAST_UTGIFT_T,
    UT_GJT,
    UT_GT_NORDISK,
    UT_ORDINER,
    UT_SP,
    UT_TFB,
    UT_TSB,
    VT
}
