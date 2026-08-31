package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag

data class Trygdeavtaler(
    val avtaleland: String?,
    val avtaletype: String? = null,
    val bostedslandbeskrivelse: String? = null
)
