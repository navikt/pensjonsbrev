package no.nav.pensjon.brev.api.model.maler

data class OmsorgEgenAutoDto(val arEgenerklaringOmsorgspoeng: Number, val arInnvilgetOmsorgspoeng: Number) {
    constructor() : this(2020, 2021)
}