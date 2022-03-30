package no.nav.pensjon.brev.api.model

enum class Sivilstand{
    ENSLIG,
    ENKE,
    GIFT,
    GIFT_LEVER_ADSKILT,
    SEPARERT,
    PARTNER,
    PARTNER_LEVER_ADSKILT,
    SEPARERT_PARTNER,
    SAMBOER1_5,
    SAMBOER3_2,
    INGEN,
}

enum class Sakstype{
    UFOEREP,
    ALDER,
}

enum class Institusjon{
    FENGSEL,
    HELSE,
    SYKEHJEM,
    INGEN,
}