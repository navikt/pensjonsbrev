@file:Suppress("unused")

package no.nav.pensjon.brev.api.model

enum class Sivilstand {
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
}

enum class Sakstype {
    AFP,
    ALDER,
    UFOEREP,
    BARNEP,
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

enum class KravAarsakType {
    SOKNAD_BT,
    TILST_DOD,
    ENDRET_OPPTJENING,
    ENDRET_INNTEKT,
    ENDRET_IFU,
}
enum class InntektFoerUfoereBegrunnelse {
    STDBEGR_12_8_2_3,
    STDBEGR_12_8_2_4,
    STDBEGR_12_8_2_5,
}
enum class EksludereBrevkode{
    UT_OPPHOER_BT_AUTO,  // PE_UT_07_200
}