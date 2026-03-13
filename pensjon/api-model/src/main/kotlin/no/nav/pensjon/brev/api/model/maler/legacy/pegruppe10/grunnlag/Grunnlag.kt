package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag


data class Grunnlag(
    val persongrunnlagsliste: List<Persongrunnlag>?,
    val persongrunnlagavdod: List<PersongrunnlagAvdod>?
)