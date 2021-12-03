package no.nav.pensjon.brev.api.model.maler

data class OmsorgEgenAutoDto(val aarEgenerklaringOmsorgspoeng: Number, val aarInnvilgetOmsorgspoeng: Number) {
    constructor() : this(2020, 2021)
}