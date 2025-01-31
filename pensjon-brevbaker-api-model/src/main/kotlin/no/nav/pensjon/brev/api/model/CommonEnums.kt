@file:Suppress("unused")

package no.nav.pensjon.brev.api.model

// Brukerens egen sivilstand tolket ut fra om beregningsresultat fra PREG
enum class Sivilstand {
    ENSLIG,
    ENKE,
    GIFT,
    GIFT_LEVER_ADSKILT, //TODO depricate, replace with BorMedSivilstand and remove after testing OpphoerBarnetilleggAuto - HH
    SEPARERT,
    PARTNER,
    PARTNER_LEVER_ADSKILT, //TODO depricate, replace with BorMedSivilstand and remove after testing OpphoerBarnetilleggAuto - HH
    SEPARERT_PARTNER,
    SAMBOER1_5, //TODO depricate, replace with BorMedSivilstand and remove after testing OpphoerBarnetilleggAuto - HH
    SAMBOER3_2, //TODO depricate, replace with BorMedSivilstand and remove after testing OpphoerBarnetilleggAuto - HH
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