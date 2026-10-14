package no.nav.pensjon.brev.api.model.maler.felles


data class Grunnlag(
    val borIUtlandet: Boolean,//pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("")
    val epsBrukerBorMed: Eps?,
) {

    enum class Eps {
        EKTEFELLE, PARTNER, SAMBOER
    }
}