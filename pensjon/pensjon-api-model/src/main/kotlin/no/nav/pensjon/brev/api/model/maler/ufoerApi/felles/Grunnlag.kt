package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles


data class Grunnlag(
    val borIUtlandet: Boolean,
    val anvendtSivilstand: AnvendtSivilstandCode?,
    val epsBrukerBorMed: Eps?,
) {

    enum class Eps {
        EKTEFELLE, PARTNER, SAMBOER
    }

    enum class AnvendtSivilstandCode {
        BOR_MED_1_5, BOR_MED_3_2, BOR_MED_EKTEFELLE, BOR_MED_REG_PARTNER, ENSLIG, ENSLIG_SEPARERT, ENSLIG_SEPARERT_PARTNER,
        GIFT_EKTEFELLE_BOR_MED_3_2, GIFT_LEVER_ADSKILT, REG_PARTNER_BOR_MED_3_2, REG_PARTNER_LEVER_ADSKILT,
        SEPARERT_BOR_MED_EKTEFELLE, SEPARERT_BOR_MED_3_2, SEPARERT_BOR_MED_PARTNER, SEPARERT_PARTNER_BOR_MED_3_2
    }
}