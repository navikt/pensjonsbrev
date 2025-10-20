package no.nav.pensjon.brev.model.alder

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
    SAMBOER_3_2, SEPARERT, SEPARERT_PARTNER, UKJENT;
}