package no.nav.pensjon.brev.ufore.api.model.vedlegg

// Speilet fra pensjon/api-model CommonEnums for bruk i ufoere-modulen, siden ufoere/api-model
// kun avhenger av brevbaker-common og ikke av pensjon/api-model.

enum class Sivilstand {
    ENSLIG,
    ENKE,
    GIFT,
    SEPARERT,
    PARTNER,
    SEPARERT_PARTNER,
}

enum class BorMedSivilstand {
    PARTNER_LEVER_ADSKILT,
    GIFT_LEVER_ADSKILT,
    EKTEFELLE,
    PARTNER,
    SAMBOER1_5,
    SAMBOER3_2,
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
    USA,
}

/**
 * Kravårsakstypene vedlegget skiller på. Mappes fra PEN sin kravårsakskode i databyggeren (del 2).
 * ANNET dekker alle øvrige kravårsaker som vedlegget ikke har spesialbehandling for.
 */
enum class Kravaarsaktype {
    SOKNAD_BT,
    ENDRET_INNTEKT,
    ENDRING_IFU,
    SIVILSTANDSENDRING,
    TILST_DOD,
    ANNET,
}
