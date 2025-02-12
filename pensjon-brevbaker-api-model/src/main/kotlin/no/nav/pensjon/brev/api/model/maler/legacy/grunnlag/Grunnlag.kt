package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag

data class Grunnlag(
    val persongrunnlagsliste: List<Persongrunnlag>?,
    val persongrunnlagavdod: List<PersongrunnlagAvdod>?,
)
