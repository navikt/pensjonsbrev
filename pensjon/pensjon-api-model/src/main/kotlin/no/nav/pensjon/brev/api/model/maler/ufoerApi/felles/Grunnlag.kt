package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles


data class Grunnlag(
    val borIUtlandet: Boolean,
    val epsBrukerBorMed: Eps?,
) {

    enum class Eps {
        EKTEFELLE, PARTNER, SAMBOER
    }
}