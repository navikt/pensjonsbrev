package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Year

@Suppress("unused")
data class OmsorgEgenAutoDto(val aarEgenerklaringOmsorgspoeng: Year, val aarInnvilgetOmsorgspoeng: Year)
