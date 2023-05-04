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
    ENDRET_IFU,
    ENDRET_INNTEKT,
    ENDRET_OPPTJENING,
    SIVILSTANDSENDRING,
    SOKNAD_BT,
    TILST_DOD,
}

enum class InntektFoerUfoereBegrunnelse {
    STDBEGR_12_8_2_3, // Forskrift standardbegrunnelser. Bruker får minsteIFU. Inntekt før uførhet fastsettes til minstenivå på 3,5 ganger grunnbeløpet fordi bruker har hatt lav eller ingen inntekt før uførhet.
    STDBEGR_12_8_2_4, // Bruker er ung ufør og får ung ufør IFU. Inntekt før uførehet fastsettes til minstenivå på 4,5 ganger grunnbeløpet fordi bruker er innvilget rettighet som ung ufør.
    STDBEGR_12_8_2_5, // Bruker får endret inntekt før uførhet til gjennomsnittet av de tre beste av de fem siste år før uførhet.
}

